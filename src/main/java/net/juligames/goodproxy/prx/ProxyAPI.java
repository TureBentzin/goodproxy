package net.juligames.goodproxy.prx;


import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.Command;
import net.juligames.goodproxy.websoc.command.v1.request.RegisterCommand;
import net.juligames.goodproxy.websoc.command.v1.response.DisplayMessageResponse;
import net.juligames.goodproxy.websoc.command.v1.response.Response;
import net.juligames.goodproxy.websoc.command.v1.request.MOTDCommand;
import net.juligames.goodproxy.websoc.command.v1.response.MOTDResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.constant.Constable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class ProxyAPI {

    public static final int TIMEOUT = 5000;
    protected static final @NotNull Logger LOGGER = LogManager.getLogger(ProxyAPI.class);

    private @Nullable Session session = null;
    private final @NotNull HashMap<Class<? extends Response>, Queue<Response>> responseQueue = new HashMap<>();
    private final @NotNull HashMap<Class<? extends Response>, Queue<Response>> unexpectedCommandQueue = new HashMap<>();
    private final @NotNull Queue<APIMessage> sendQueue = new ArrayDeque<>();
    private boolean waiting = false;
    private int id = 0;

    private @NotNull String messageSet = "english";
    private @Nullable Credentials storedCredentials;

    public @NotNull Session getSession() {
        if (session == null) {
            throw new IllegalStateException("Session is null");
        }

        if (!session.isOpen()) {
            throw new IllegalStateException("Session is closed");
        }

        return session;
    }

    /**
     * @see ProxyAPIFactory
     */
    protected ProxyAPI() {

    }

    public boolean checkSession() {
        try {
            Session session1 = getSession();
            return true;
        } catch (IllegalStateException ignored) {
            return false;
        }
    }

    public synchronized void incomingResponse(@NotNull Response response) {
        Class<? extends Response> responseClazz = response.getClass();
        if (!responseQueue.containsKey(responseClazz)) {
            responseQueue.put(responseClazz, new ArrayDeque<>());
        }

        responseQueue.get(responseClazz).add(response);
        LOGGER.debug("Received response of type: {}", responseClazz.getSimpleName());
    }

    public synchronized void incomingUnexpectedCommand(@NotNull Response response) {
        Class<? extends Response> responseClazz = response.getClass();
        if (!unexpectedCommandQueue.containsKey(responseClazz)) {
            unexpectedCommandQueue.put(responseClazz, new ArrayDeque<>());
        }

        unexpectedCommandQueue.get(responseClazz).add(response);
        LOGGER.debug("Received unexpected command of type: {}", responseClazz.getSimpleName());
    }


    public @NotNull Future<String> motd() {
        BankingAPI.stageCommand(this, new MOTDCommand());

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            MOTDResponse poll = waitForResponse(MOTDResponse.class);
            return poll.getMOTD();
        });
        return future.orTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public @NotNull Future<DisplayMessage> register(@NotNull Credentials credentials) {
        BankingAPI.stageCommand(this, new RegisterCommand(credentials));
        return awaitMessage();
    }

    public @NotNull Future<DisplayMessage> authenticate(@NotNull Credentials credentials) {
        BankingAPI.stageCommand(this, new RegisterCommand(credentials));
        return awaitMessage(displayMessageResponse -> {
            this.id = displayMessageResponse.getSource().getId();
        });
    }

    private @NotNull CompletableFuture<DisplayMessage> awaitMessage() {
        return awaitMessage(displayMessageResponse -> {
        });
    }

    private @NotNull CompletableFuture<DisplayMessage> awaitMessage(@NotNull Consumer<DisplayMessageResponse> additionalAction) {
        return CompletableFuture.supplyAsync(() -> {
            DisplayMessageResponse displayMessageResponse = waitForResponse(DisplayMessageResponse.class);
            additionalAction.accept(displayMessageResponse);
            return displayMessageResponse.getMessage(messageSet);
        }).orTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private <T extends Response> @NotNull T waitForResponse(@NotNull Class<T> type) {
        @NotNull Logger logger = LogManager.getLogger();
        if (!responseQueue.containsKey(type)) {
            responseQueue.put(type, new ArrayDeque<>());
        }
        Queue<Response> responses = responseQueue.get(type);
        while (responses.isEmpty()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error("wait was interrupted", e);
            }
        }
        //noinspection unchecked
        return (T) responses.poll();
    }


    public @NotNull Map<Class<? extends Response>, Queue<Response>> copyResponseQueue() {
        return Map.copyOf(responseQueue);
    }

    public @NotNull Queue<APIMessage> getSendQueue() {
        return sendQueue;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public void awaitSession() {
        while (!checkSession()) {
            try {
                wait(1000); //auto check
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void populate(@NotNull Session session) {
        this.session = session;
        //notifyAll();
    }

    public int getId() {
        return id;
    }

    @SuppressWarnings("removal")
    @Override
    protected void finalize() throws Throwable {
        getSession().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "closed because api was collected!"));
    }
}
