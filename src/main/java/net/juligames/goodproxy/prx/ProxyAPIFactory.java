package net.juligames.goodproxy.prx;

import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.WebsocketManager;
import org.jetbrains.annotations.NotNull;

public class ProxyAPIFactory {

    private ProxyAPIFactory() {
        throw new IllegalStateException("");
    }

    public static final @NotNull WebsocketManager websocketManager = new WebsocketManager();

    public static @NotNull ProxyAPI create() {
        final ProxyAPIImpl proxyAPI = new ProxyAPIImpl();
        websocketManager.openNewConnection(proxyAPI::populate, true);
        proxyAPI.awaitSession();

        BankingAPI.register(proxyAPI);
        return proxyAPI;
    }
}
