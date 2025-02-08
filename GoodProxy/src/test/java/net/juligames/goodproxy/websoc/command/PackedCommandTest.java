package net.juligames.goodproxy.websoc.command;


import net.juligames.goodproxy.websoc.action.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
class PackedCommandTest {

    private static final @NotNull Logger LOGGER = LogManager.getLogger(PackedCommandTest.class);

    @Test
    void toJsonObject() {
        PackedCommand packedCommand = PackedCommand.create(Action.AUTHENTICATE, "username", "password");
        String json = packedCommand.toJson();
        LOGGER.debug("Json: {}", json);


    }

}
