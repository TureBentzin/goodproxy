package net.juligames.goodproxy.displaymessage.source;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class HardcodeMessages {

    private static final @NotNull Map<String, Map<String, String>> MESSAGE_SETS = new HashMap<>();
    private static final @NotNull String DEFAULT_SET = "default";

    static {
        detectAndLoadMessageSets();
    }

    private HardcodeMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static void detectAndLoadMessageSets() {
        ClassLoader classLoader = HardcodeMessages.class.getClassLoader();
        try {
            Enumeration<java.net.URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                java.net.URL resource = resources.nextElement();
                java.io.File dir = new java.io.File(resource.getPath());
                if (dir.isDirectory()) {
                    for (String fileName : Objects.requireNonNull(dir.list())) {
                        if (fileName.startsWith("messages_") && fileName.endsWith(".json")) {
                            String setId = fileName.substring(9, fileName.length() - 5);
                            loadMessageSet(setId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to detect message sets", e);
        }
    }

    private static void loadMessageSet(@NotNull String setId) {
        String fileName = "messages_" + setId + ".json";
        try (InputStream inputStream = HardcodeMessages.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Map<String, String> messages = gson.fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
            MESSAGE_SETS.put(setId, Collections.unmodifiableMap(messages));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load message set: " + setId, e);
        }
    }

    public static @NotNull String getMessage(@NotNull String key) {
        return getMessage(key, DEFAULT_SET);
    }

    public static @NotNull String getMessage(@NotNull String key, @NotNull String setId) {
        return MESSAGE_SETS.getOrDefault(setId, Collections.emptyMap()).getOrDefault(key, key);
    }

    public static @NotNull Set<String> getAvailableMessageSets() {
        return MESSAGE_SETS.keySet();
    }

    public static @NotNull Map<String, String> getAllMessages(@NotNull String setId) {
        return MESSAGE_SETS.getOrDefault(setId, Collections.emptyMap());
    }
}
