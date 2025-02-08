package net.juligames.goodproxy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.image.LookupOp;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class Main {

    protected static final @NotNull Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(@NotNull String @NotNull [] args) {

        LOGGER.info("Hello World!");
    }
}