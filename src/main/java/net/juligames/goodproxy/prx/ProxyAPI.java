package net.juligames.goodproxy.prx;


import jakarta.websocket.Session;
import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.Command;
import net.juligames.goodproxy.websoc.command.v1.response.Response;
import net.juligames.goodproxy.websoc.command.v1.request.MOTDCommand;
import net.juligames.goodproxy.websoc.command.v1.response.MOTDResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class ProxyAPI {

    public static final int TIMEOUT = 5000;

    protected static final @NotNull Logger LOGGER = LogManager.getLogger(ProxyAPI.class);

    private @Nullable Session session;

    private final @NotNull HashMap<Class<? extends Response>, Queue<Response>> responseQueue = new HashMap<>();

    private final @NotNull Queue<APIMessage> sendQueue = new ArrayDeque<>();

    private boolean waiting = false;

    public @NotNull Session getSession() {
        if (session == null) {
            throw new IllegalStateException("Session is null");
        }

        if (!session.isOpen()) {
            throw new IllegalStateException("Session is closed");
        }

        return session;
    }

    public synchronized void incomingResponse(@NotNull Response response) {
        Class<? extends Response> responseClazz = response.getClass();
        if (!responseQueue.containsKey(responseClazz)) {
            responseQueue.put(responseClazz, new ArrayDeque<>());
        }

        responseQueue.get(responseClazz).add(response);
        LOGGER.debug("Received response of type: {}", responseClazz);
    }


    public @NotNull CompletableFuture<String> motd() {
        BankingAPI.stageCommand(this, new MOTDCommand());

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            @NotNull Logger logger = LogManager.getLogger();
            long timeStart = System.currentTimeMillis();
            Queue<Response> responses = responseQueue.get(MOTDResponse.class);
            while (responses.isEmpty()) {
                try {
                    wait(200);
                } catch (InterruptedException e) {
                    logger.error("wait was interrupted", e);
                }
            }
            MOTDResponse poll = (MOTDResponse) responses.poll();
            return poll.getMOTD();
        });
        return future.orTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
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
}
