package net.juligames.goodproxy.json;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class ActionDeserializer implements JsonDeserializer<Action> {
    @Override
    public @NotNull Action deserialize(@NotNull JsonElement jsonElement, @NotNull Type type, @NotNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Objects.requireNonNull(Action.fromString(jsonElement.getAsString()));
    }
}
