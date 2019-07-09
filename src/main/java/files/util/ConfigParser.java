package files.util;

import files.Config;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This Class is used to parse the given Yaml-File sinto Java-Objects
 */
public class ConfigParser {
    /**
     * Parses the given Yaml-Files into Java Objects used by this Project
     *
     * @param p Path to the Yaml-File that shall be parsed
     * @return Returns a Config used to represent Notesheets
     * @throws IOException If the file p specifies cannot be found
     */
    public static Config parseYamlFile(Path p) throws IOException {
        Yaml yaml = new Yaml(new Constructor(Config.class));

        try (InputStream stream = Files.newInputStream(p)) {
            Config config = yaml.load(stream);
            config.init();
            return config;
        }
    }
}