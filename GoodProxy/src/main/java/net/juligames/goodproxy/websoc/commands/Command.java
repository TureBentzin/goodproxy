package net.juligames.goodproxy.websoc.commands;


import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public interface Command {


    @NotNull PackedCommand pack();
}
