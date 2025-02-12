package net.juligames.goodproxy.prx;


import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.displaymessage.DisplayMessageWithPayload;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.util.ThrowingFunction;
import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.v1.request.*;
import net.juligames.goodproxy.websoc.command.v1.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static net.juligames.goodproxy.websoc.BankingAPI.TIMEOUT;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
@SuppressWarnings("unused")
public class ProxyAPIImpl implements ProxyAPI {
    protected static final @NotNull Logger LOGGER = LogManager.getLogger(ProxyAPIImpl.class);

    private @Nullable Session session = null;
    private final @NotNull BlockingQueue<APIMessage> requestQueue = new LinkedBlockingQueue<>();
    private final @NotNull Queue<CompletableFuture<Response>> responseFutures = new ConcurrentLinkedQueue<>();
    private final @NotNull ConcurrentLinkedQueue<Response> unexpectedCommandQueue = new ConcurrentLinkedQueue<>();
    private final @NotNull ExecutorService requestExecutor = Executors.newSingleThreadExecutor();
    private boolean waiting = false;
    private int id = 0;

    private final @Nullable String messageSet = null; //TODO
    private @Nullable Credentials storedCredentials;


    private @NotNull Credentials credentials() {
        if (storedCredentials == null) {
            throw new IllegalStateException("Credentials are null. Use #authenticate first!");
        }
        return storedCredentials;
    }

    @Override
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
    protected ProxyAPIImpl() {

    }

    /**
     * Checks if the session is still open
     *
     * @return true if the session is open
     */
    @Override
    public boolean checkSession() {
        try {
            getSession();
            return true;
        } catch (IllegalStateException ignored) {
            return false;
        }
    }

    /**
     * INTERNAL
     * <p>
     * Receives a message from the server
     *
     * @param response the response
     */
    public void incomingResponse(@NotNull Response response) {
        CompletableFuture<Response> future = responseFutures.poll();
        if (future != null) {
            future.complete(response);
        } else {
            LOGGER.warn("Received unexpected response (dropping): {}", response);
        }
    }

    private @NotNull <T> CompletableFuture<T> async(@NotNull Callable<T> callable) {
        try {
            return supplyAsync(() -> {
                Logger logger = LogManager.getLogger();
                try {
                    return callable.call();
                } catch (Exception e) {
                    logger.debug("Failed to execute async task", e);
                    throw new RuntimeException("Failed to execute async task", e);
                }
            });
        } catch (Exception e) {
            LOGGER.throwing(e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private @NotNull <T> CompletableFuture<T> async(@NotNull ThrowingFunction<Logger, T, Exception> callableWithLogger) {
        try {
            return supplyAsync(() -> {
                Logger logger = LogManager.getLogger();
                try {
                    return callableWithLogger.apply(logger);
                } catch (Exception e) {
                    logger.debug("Failed to execute async task", e);
                    throw new RuntimeException("Failed to execute async task", e);
                }
            });
        } catch (Exception e) {
            LOGGER.throwing(e);
            return CompletableFuture.failedFuture(e);
        }
    }


    /**
     * Retrieves the MOTD from the server
     *
     * @return the MOTD
     */
    @Override
    public @NotNull Future<String> motd() {
        return async(() -> ((MOTDResponse) BankingAPI.stageCommand(this, new MOTDCommand()).get()).getMOTD());
    }

    /**
     * Registers a new user
     *
     * @param credentials the credentials
     * @return the response
     */
    @Override
    public @NotNull Future<DisplayMessage> register(@NotNull Credentials credentials) {
        return awaitMessage(BankingAPI.stageCommand(this, new RegisterCommand(credentials)));
    }

    /**
     * Authenticates a user
     *
     * @param credentials the credentials
     * @return the response
     */
    @Override
    public @NotNull Future<DisplayMessage> authenticate(@NotNull Credentials credentials) {
        return awaitMessage(BankingAPI.stageCommand(this, new AuthenticateCommand(credentials)), displayMessageResponse -> {
            id = Integer.parseInt(Objects.requireNonNull(displayMessageResponse.getSource().getValue3(), "id is null"));
            storedCredentials = credentials;
        });
    }

    @Override
    public @NotNull Future<DisplayMessage> logout() {
        return logout(credentials());
    }

    /**
     * Performs a logout
     *
     * @param credentials the credentials
     * @return the response
     */
    @Override
    public @NotNull Future<DisplayMessage> logout(@NotNull Credentials credentials) {
        return awaitMessage(BankingAPI.stageCommand(this, new LogoutCommand(credentials)));
    }

    @Override
    public @NotNull Future<DisplayMessageWithPayload<Double>> balance() {
        return balance(credentials());
    }

    /**
     * Retrieves the balance of a user
     *
     * @param credentials the credentials
     * @return the response
     */
    @Override
    public @NotNull Future<DisplayMessageWithPayload<Double>> balance(@NotNull Credentials credentials) {
        return awaitMessageWithPayload(BankingAPI.stageCommand(this, new BalanceCommand(credentials)), Double::parseDouble);
    }

    @Override
    public @NotNull Future<DisplayMessage> pay(@NotNull String destination, int amount) {
        return pay(credentials(), destination, amount);
    }

    @Override
    public @NotNull Future<DisplayMessage> pay(@NotNull Credentials credentials, @NotNull String destination, int amount) {
        return awaitMessage(BankingAPI.stageCommand(this, new PayCommand(credentials, destination, amount)));
    }

    @Override
    public @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox() {
        return getInbox(credentials());
    }

    @Override
    public @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox(@NotNull Credentials credentials) {
        return getInboxInternal(credentials, false);
    }

    @Override
    public @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox(boolean privateMessage) {
        return getInbox(credentials(), privateMessage);
    }

    /**
     * Returns number of messages in inbox
     * @param credentials   the credentials
     * @return the number of messages in inbox: (0 - n) (n messages in box)
     */
    @Override
    public @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox(@NotNull Credentials credentials, boolean privateMessage) {
        return getInboxInternal(credentials, privateMessage);
    }

    /**
     * Returns number of messages in inbox
     * @param credentials   the credentials
     * @return the number of messages in inbox: (0 - n) (n messages in box)
     */
    private @NotNull CompletableFuture<DisplayMessageWithPayload<Integer>> getInboxInternal(@NotNull Credentials credentials, boolean privateMessage) {
        return awaitMessageWithPayload(BankingAPI.stageCommand(this, new GetInboxCommand(credentials, privateMessage)), payload -> {
            return Integer.parseInt(Objects.requireNonNull(payload, "payload is null"));
        });
    }

    @Override
    public @NotNull Future<InboxResponse> getInboxMessage(@NotNull Credentials credentials, int messageID) {
        return getInboxMessage(credentials, messageID, false);
    }

    @Override
    public @NotNull Future<InboxResponse> getInboxMessage(@NotNull Credentials credentials, int messageID, boolean privateMessage) {
        return getInboxMessageInternal(credentials, messageID, privateMessage);
    }

    private @NotNull CompletableFuture<InboxResponse> getInboxMessageInternal(@NotNull Credentials credentials, int messageID, boolean privateMessage) {
        return async(() -> (InboxResponse) BankingAPI.stageCommand(this, new GetInboxCommand(credentials, messageID, privateMessage)).get());
    }

    @Override
    public @NotNull Future<List<String>> getInboxAll(@NotNull Credentials credentials) {
        return getInboxAll(credentials, false);
    }


    @Override
    public @NotNull Future<List<String>> getInboxAll(@NotNull Credentials credentials, boolean privateMessage) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        return async(logger -> {
            return getInboxInternal(credentials, privateMessage).thenCompose(messageWithPayload -> {
                int messageCount = messageWithPayload.getPayload();
                List<String> messages = new ArrayList<>();
                for (int i = 0; i < messageCount; i++) {
                    getInboxMessageInternal(credentials, i, privateMessage).thenAccept(inboxResponse -> {
                        messages.add(inboxResponse.getMessage());
                    });
                }
                return CompletableFuture.completedFuture(messages);
            }).get();
        });
    }

    @Override
    public @NotNull Future<EchoResponse> echo(@NotNull String message) {
        return async(() -> ((EchoResponse) BankingAPI.stageCommand(this, new EchoCommand(message)).get()));
    }

    @Override
    public @NotNull Future<EchoResponse> echo() {
        return async(() -> ((EchoResponse) BankingAPI.stageCommand(this, new EchoCommand()).get()));
    }

    /// INTERNAL


    private @NotNull CompletableFuture<DisplayMessage> awaitMessage(@NotNull CompletableFuture<Response> future) {
        return awaitMessage(future, displayMessageResponse -> {
        });
    }

    private @NotNull <T> CompletableFuture<DisplayMessageWithPayload<T>> awaitMessageWithPayload(@NotNull CompletableFuture<Response> future, @NotNull Consumer<DisplayMessageResponse> additionalAction, @NotNull Function<String, T> payloadConverter) {
        return async(() -> {
            DisplayMessageResponse displayMessageResponse = (DisplayMessageResponse) future.get();
            additionalAction.accept(displayMessageResponse);
            return displayMessageResponse.getMessageWithPayload(messageSet, payloadConverter);
        });
    }

    private @NotNull CompletableFuture<DisplayMessageWithPayload<String>> awaitMessageWithPayload(@NotNull CompletableFuture<Response> future, @NotNull Consumer<DisplayMessageResponse> additionalAction) {
        return awaitMessageWithPayload(future, additionalAction, s -> s);
    }

    private @NotNull <T> CompletableFuture<DisplayMessageWithPayload<T>> awaitMessageWithPayload(@NotNull CompletableFuture<Response> future, @NotNull Function<String, T> payloadConverter) {
        return awaitMessageWithPayload(future, displayMessageResponse -> {
        }, payloadConverter);
    }

    private @NotNull <T> CompletableFuture<DisplayMessageWithPayload<String>> awaitMessageWithPayload(@NotNull CompletableFuture<Response> future) {
        return awaitMessageWithPayload(future, displayMessageResponse -> {
        }, s -> s);
    }

    private @NotNull CompletableFuture<DisplayMessage> awaitMessage(@NotNull CompletableFuture<Response> future, @NotNull Consumer<DisplayMessageResponse> additionalAction) {
        return async(() -> {
            DisplayMessageResponse displayMessageResponse = (DisplayMessageResponse) future.get();
            additionalAction.accept(displayMessageResponse);
            return displayMessageResponse.getMessage(messageSet);
        });
    }


    @Override
    public @NotNull Queue<APIMessage> copyRequestQueue() {
        return new ArrayDeque<>(requestQueue);
    }

    public @NotNull BlockingQueue<APIMessage> getRequestQueue() {
        return requestQueue;
    }

    public void addResponse(@NotNull CompletableFuture<Response> future) {
        responseFutures.add(future);
    }

    @Override
    public boolean isWaiting() {
        return waiting;
    }

    @Override
    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    @Override
    public void close() {
        BankingAPI.unregister(this);
    }

    @Override
    public void awaitSession() {
        while (!checkSession()) {
            try {
                wait(1000); //auto check
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void janitor() {
        requestQueue.clear();
        responseFutures.forEach(future -> future.completeExceptionally(new IllegalStateException("Janitor cleanup")));
        responseFutures.clear();
        unexpectedCommandQueue.clear();
    }

    public void populate(@NotNull Session session) {
        this.session = session;
        //notifyAll();
    }

    @Override
    public int getId() {
        return id;
    }

    @SuppressWarnings("removal")
    @Override
    protected void finalize() throws Throwable {
        getSession().close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "closed because api was collected!"));
    }
}
