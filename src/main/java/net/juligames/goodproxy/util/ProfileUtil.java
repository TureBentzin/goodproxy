package net.juligames.goodproxy.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProfileUtil {

    private final @NotNull Environment environment;

    public ProfileUtil(@NotNull Environment environment) {
        this.environment = environment;
    }

    /**
     * Checks if a specific profile is active.
     *
     * @param profile the profile name to check
     * @return true if the profile is active, false otherwise
     */
    public boolean isProfileActive(@NotNull String profile) {
        return Arrays.asList(environment.getActiveProfiles()).contains(profile);
    }

    /**
     * Retrieves a list of all active profiles.
     *
     * @return List of active profile names
     */
    public @NotNull List<String> getAllActiveProfiles() {
        return Arrays.asList(environment.getActiveProfiles());
    }
}
