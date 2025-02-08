package net.juligames.goodproxy.websoc.command;


import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public interface Command {

    @NotNull Action getResponseAction();

    @NotNull PackedCommand pack();
}
