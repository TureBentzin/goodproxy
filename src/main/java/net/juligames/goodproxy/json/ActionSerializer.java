package net.juligames.goodproxy.json;


import com.google.gson.*;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class ActionSerializer implements JsonSerializer<Action> {
    @Override
    public @NotNull JsonElement serialize(@NotNull Action action, @NotNull Type type, @NotNull JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(action.getActionString());
    }

}
