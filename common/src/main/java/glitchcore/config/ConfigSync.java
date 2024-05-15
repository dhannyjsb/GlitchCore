/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.config;

import glitchcore.network.SyncConfigPacket;
import glitchcore.util.Environment;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigSync
{
    private static Map<String, Config> configs = new HashMap<>();

    public static void register(Config config)
    {
        String relative = Environment.getConfigPath().relativize(config.getPath()).toString();
        configs.put(relative, config);

    }

    public static Stream<SyncConfigPacket> createPackets()
    {
        return configs.entrySet().stream().map(e -> {
            var config = e.getValue();

            // Reload the config from the filesystem, but do not save it
            config.read();
            config.load();

            return new SyncConfigPacket(e.getKey(), e.getValue().encode().getBytes(StandardCharsets.UTF_8));
        });
    }

    public static void reload(String pathString, String toml)
    {
        Path path = Paths.get(pathString);
        String normalizedPath = path.toString();
        var config = configs.get(normalizedPath);
        if (config == null) {
            throw new NullPointerException("GlitchCore Config path is null for: " + normalizedPath);
        }
        config.parse(toml);
        config.load();
    }
}
