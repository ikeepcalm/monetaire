package dev.ua.ikeepcalm.monetaire;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class Configuration {

    public static @NotNull FileConfiguration configuration;

    public Configuration(@NotNull FileConfiguration file){
        configuration = file;
    }

    public static @NotNull FileConfiguration getConfiguration() {
        return configuration;
    }
}
