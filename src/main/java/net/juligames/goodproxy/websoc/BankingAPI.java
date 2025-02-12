package net.juligames.goodproxy.websoc;


import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import net.juligames.goodproxy.prx.ProxyAPIImpl;
import net.juligames.goodproxy.websoc.command.Command;
import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.v1.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class BankingAPI {

    public static final long TIMEOUT = 5000;


    public static final @NotNull String API_URL = "ws://befator.befatorinc.de:5000/banking";
    public static final @NotNull Logger LOGGER = LogManager.getLogger(BankingAPI.class);
    private static final @NotNull HashMap<String, ProxyAPIImpl> activeAPISessions = new HashMap<>();


    /**
     * INTERNAL
     * Handles incoming messages from the server
     * and forwards them to the correct ProxyAPI
     * instance.
     *
     * @param session  The session the message was received on
     * @param incoming The incoming message
     * @throws IOException If the message could not be handled
     */
    protected synchronized static void handleCommand(@NotNull Session session, @NotNull APIMessage incoming) throws IOException {
        LOGGER.debug("Handling incoming message: {}", incoming);
        final String sessionID = session.getId();
        if (!activeAPISessions.containsKey(sessionID)) {
            LOGGER.warn("No active proxyApi for this session. Ignoring and closing session!");
            session.close(new CloseReason(CloseReason.CloseCodes.NOT_CONSISTENT, "No active proxyApi for this session"));
        }
        ProxyAPIImpl proxyAPI = activeAPISessions.get(sessionID);

        if (incoming.getId() != -1 && incoming.getId() != proxyAPI.getId()) {
            LOGGER.debug("Message dropped, because of unrecognized receiver: {}@ (we are {}@)", incoming.getId(), proxyAPI.getId());
            return;
        }

        try {
            Response response = Response.fromMessage(incoming);
            if (!proxyAPI.isWaiting() && response.probablyUnexpected()) {
                LOGGER.warn("Incoming message was unexpected. Storing to queue for handeling!");
                proxyAPI.incommingUnexpectedResponse(response);
                return; //do not fire next!
            }
            proxyAPI.incomingResponse(response);

        } catch (Exception e) {
            LOGGER.warn("Failed to determine response from incoming message", e);
        }

        proxyAPI.setWaiting(false);

        //send next message
        fireNext(proxyAPI);
    }

    /**
     * Sends a command to the server
     *
     * @param proxyAPI The proxyAPI to send the command with
     * @param command  The command to send
     */
    @CheckReturnValue
    public synchronized static @NotNull CompletableFuture<Response> stageCommand(@NotNull ProxyAPIImpl proxyAPI, @NotNull APIMessage command) {
        CompletableFuture<Response> future = new CompletableFuture<>();
        boolean blocked = proxyAPI.isWaiting();
        if (!blocked) {
            //fire now
            sendMessage(proxyAPI, command);
        } else {
            final Queue<APIMessage> queue = proxyAPI.getRequestQueue();
            queue.add(command);
            LOGGER.info("Staged {} for sending. Current Position: {}", command, queue.size());
        }
        proxyAPI.addResponse(future);
        future.orTimeout(TIMEOUT, MILLISECONDS);
        return future;

    }

    /**
     * Sends a command to the server
     *
     * @param proxyAPI proxyAPI to send the command with
     * @param command  The command to send
     */
    @CheckReturnValue
    public static @NotNull CompletableFuture<Response> stageCommand(@NotNull ProxyAPIImpl proxyAPI, @NotNull Command command) {
        return stageCommand(proxyAPI, command.pack());
    }

    private static void sendMessage(@NotNull ProxyAPIImpl proxyAPI, @NotNull APIMessage command) {
        Session session = proxyAPI.getSession();
        if (proxyAPI.isWaiting()) {
            throw new IllegalStateException("Cant sent message when waiting!");
        }
        proxyAPI.setWaiting(true);
        command.setId(proxyAPI.getId());
        String json;
        try {
            json = command.toJson();
        } catch (Exception e) {
            LOGGER.error("Failed to serialize message {}", command, e);
            return;
        }

        LOGGER.debug("Sending message: {} - {}", command, json);

        try {
            session.getBasicRemote().sendText(command.toJson());
        } catch (Exception e) {
            LOGGER.error("Failed to send message", e);
        }
    }

    private synchronized static void fireNext(@NotNull ProxyAPIImpl proxyAPI) {
        final Queue<APIMessage> queue = proxyAPI.getRequestQueue();
        if (!queue.isEmpty()) {
            sendMessage(proxyAPI, queue.poll());
        } else {
            LOGGER.debug("Cant fire next message, because queue is empty!");
        }

    }

    public static void register(@NotNull ProxyAPIImpl proxyAPI) {
        proxyAPI.awaitSession();

        Session session = proxyAPI.getSession();
        activeAPISessions.putIfAbsent(session.getId(), proxyAPI);
    }

    public static void unregister(@NotNull ProxyAPIImpl proxyAPI) {
        unregister(proxyAPI.getSession().getId());
    }

    public static void unregister(@NotNull String sessionID) {
        ProxyAPIImpl remove = activeAPISessions.remove(sessionID);
        if (remove == null) {
            LOGGER.warn("Tried to unregister a proxyAPI that was not registered!");
        } else {
            remove.janitor();
            remove.close();
        }

    }


}
