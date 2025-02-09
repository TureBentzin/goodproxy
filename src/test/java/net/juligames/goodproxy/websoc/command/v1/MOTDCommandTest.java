package net.juligames.goodproxy.websoc.command.v1;

import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.WebsocketManager;
import net.juligames.goodproxy.websoc.command.v1.request.MOTDCommand;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
class MOTDCommandTest {

    private static final @NotNull WebsocketManager websocketManager = new WebsocketManager();


    @Test
    void motd() {


        boolean b = websocketManager.openNewConnection(session -> {
            MOTDCommand motdCommand = new MOTDCommand();
            assertNotNull(motdCommand.pack());

            BankingAPI.sendCommand(session, motdCommand);

            long time = System.currentTimeMillis();

            while (System.currentTimeMillis() - time < 5000) {
                if (BankingAPI.getMotd() != null) {
                    break;
                }
            }
        });
    }
}