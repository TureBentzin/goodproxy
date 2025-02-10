package net.juligames.goodproxy.websoc.action;

import net.juligames.goodproxy.prx.StagingTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    private static final @NotNull Logger LOGGER = LogManager.getLogger(ActionTest.class);


    @Test
    void uid() {
        //iterate over all actions check if they have a unique id
        //re grab the action by id and check if it is the same as the original

        for (Action action : Action.values()) {
            final String uid = action.uid();
            LOGGER.info("Action: {} UID: {}", action, uid);
            assertEquals(action, Action.fromUID(uid));
        }


    }
}