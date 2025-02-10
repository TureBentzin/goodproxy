package net.juligames.goodproxy.displaymessage.source;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HardcodeMessagesTest {

    private static final @NotNull Logger LOGGER = LogManager.getLogger(HardcodeMessagesTest.class);

    @Test
    void getMessage() {
        String message = HardcodeMessages.getMessage("auth.success");
        assertNotNull(message);
        assertInstanceOf(String.class, message);
    }

    @Test
    void testGetMessage() {
        String message = HardcodeMessages.getMessage("auth.success", "default");
        assertNotNull(message);
        assertInstanceOf(String.class, message);
    }

    @Test
    void getAvailableMessageSets() {
        Set<String> availableMessageSets = HardcodeMessages.getAvailableMessageSets();
        LOGGER.info("found sets: {}", availableMessageSets);
        assertFalse(availableMessageSets.isEmpty());
    }

    @Test
    void getAllMessages() {
        Set<String> availableMessageSets = HardcodeMessages.getAvailableMessageSets();

        for (String availableMessageSet : availableMessageSets) {
            Map<String, String> messages = HardcodeMessages.getAllMessages(availableMessageSet);
            assertNotNull(messages);
            assertFalse(messages.isEmpty());

            messages.forEach((key, value) -> {
                LOGGER.debug("{}@{} = {}", key, availableMessageSet, value);
                assertNotNull(key);
                assertNotNull(value);
                assertInstanceOf(String.class, value);
            });
        }
    }

    @Test
    void testAllKeysPresentInEverySet() {
        Set<String> availableMessageSets = HardcodeMessages.getAvailableMessageSets();
        assertFalse(availableMessageSets.isEmpty());

        String referenceSet = "default";
        Map<String, String> referenceMessages = HardcodeMessages.getAllMessages(referenceSet);
        assertNotNull(referenceMessages);
        assertFalse(referenceMessages.isEmpty());

        for (String set : availableMessageSets) {
            Map<String, String> messages = HardcodeMessages.getAllMessages(set);
            assertNotNull(messages);
            assertFalse(messages.isEmpty());
            assertEquals(referenceMessages.keySet(), messages.keySet(), "Message keys mismatch between " + referenceSet + " and " + set);
        }
    }
}
