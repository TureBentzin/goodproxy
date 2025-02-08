package net.juligames.goodproxy.websoc;


import jakarta.websocket.Session;
import net.juligames.goodproxy.websoc.command.Command;
import net.juligames.goodproxy.websoc.command.PackedCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class BankingAPI {

    public static final @NotNull String API_URL = "ws://befator.befatorinc.de:5000/banking";

    public static final @NotNull Logger LOGGER = LogManager.getLogger(BankingAPI.class);


    public static @NotNull Command handleCommand(@NotNull Session session, @NotNull PackedCommand incoming) {
        LOGGER.debug("Handling command: {}", incoming);

        return null;
    }

    public static void sendCommand(@NotNull Session session, @NotNull PackedCommand command) {
        LOGGER.debug("Sending command: {}", command);
        try {
            session.getBasicRemote().sendText(command.toJson());
        } catch (Exception e) {
            LOGGER.error("Failed to send command", e);
        }
    }

    public static void sendCommand(@NotNull Session session, @NotNull Command command) {
        sendCommand(session, command.pack());
    }



}
