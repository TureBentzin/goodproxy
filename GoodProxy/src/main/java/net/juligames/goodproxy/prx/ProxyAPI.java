package net.juligames.goodproxy.prx;


import jakarta.websocket.Session;
import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.command.Command;
import net.juligames.goodproxy.websoc.command.v1.request.MOTDCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class ProxyAPI {

    private @Nullable Session session;

    private @NotNull Session getSession() {
        if (session == null) {
            throw new IllegalStateException("Session is null");
        }

        if (!session.isOpen()) {
            throw new IllegalStateException("Session is closed");
        }

        return session;
    }

    public void incomingCommand(@NotNull Command command) {

    }



    public @NotNull CompletableFuture<String> motd() {
        BankingAPI.sendCommand(getSession(), new MOTDCommand());

        return CompletableFuture.supplyAsync(() -> {
            long time = System.currentTimeMillis();

            while (System.currentTimeMillis() - time < 5000) {
                if (BankingAPI.getMotd() != null) {
                    return BankingAPI.getMotd();
                }
            }

            return null;
        });
    }
}
