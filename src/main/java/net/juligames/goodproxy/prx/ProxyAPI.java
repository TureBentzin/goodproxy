package net.juligames.goodproxy.prx;

import jakarta.websocket.Session;
import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.displaymessage.DisplayMessageWithPayload;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.v1.response.EchoResponse;
import net.juligames.goodproxy.websoc.command.v1.response.InboxResponse;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Future;

public interface ProxyAPI extends Closeable {

    @NotNull Session getSession();

    boolean checkSession();

    @NotNull Future<String> motd();

    @NotNull Future<DisplayMessage> register(@NotNull Credentials credentials);

    @NotNull Future<DisplayMessage> authenticate(@NotNull Credentials credentials);

    @NotNull Future<DisplayMessage> logout();

    @NotNull Future<DisplayMessage> logout(@NotNull Credentials credentials);

    @NotNull Future<DisplayMessageWithPayload<Double>> balance();

    @NotNull Future<DisplayMessageWithPayload<Double>> balance(@NotNull Credentials credentials);

    @NotNull Future<DisplayMessage> pay(@NotNull String destination, int amount);

    @NotNull Future<DisplayMessage> pay(@NotNull Credentials credentials, @NotNull String destination, int amount);

    @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox();

    @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox(@NotNull Credentials credentials);

    @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox(boolean privateMessage);

    @NotNull Future<DisplayMessageWithPayload<Integer>> getInbox(@NotNull Credentials credentials, boolean privateMessage);

    @NotNull Future<InboxResponse> getInboxMessage(@NotNull Credentials credentials, int messageID);

    @NotNull Future<InboxResponse> getInboxMessage(@NotNull Credentials credentials, int messageID, boolean privateMessage);

    @NotNull Future<List<String>> getInboxAll(@NotNull Credentials credentials);

    @NotNull Future<List<String>> getInboxAll(@NotNull Credentials credentials, boolean privateMessage);

    @NotNull Future<EchoResponse> echo(@NotNull String message);

    @NotNull Future<EchoResponse> echo();

    @NotNull Queue<APIMessage> copyRequestQueue();

    boolean isWaiting();

    void setWaiting(boolean waiting);

    void awaitSession();

    /**
     * Cleans up remaining data
     */
    void janitor();

    int getId();
}
