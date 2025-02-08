package net.juligames.goodproxy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class GoodProxy {

    protected static final @NotNull Logger LOGGER = LogManager.getLogger(GoodProxy.class);

    public static void main(@NotNull String @NotNull [] args) {

        LOGGER.info("Hello World!");
    }
}