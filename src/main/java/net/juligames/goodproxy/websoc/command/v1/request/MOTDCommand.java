package net.juligames.goodproxy.websoc.command.v1.request;


import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.Command;
import net.juligames.goodproxy.websoc.command.PackedCommand;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class MOTDCommand implements Command {
    @Override
    public @NotNull PackedCommand pack() {
        return PackedCommand.create(Action.MOTD);
    }
}
