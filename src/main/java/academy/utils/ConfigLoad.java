package academy.utils;

import academy.exceptions.ConfigurationLoadException;
import academy.models.config.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class ConfigLoad {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ConfigLoad() {}

    public static Configuration loadFromJson(String pathString) {
        try {
            Path path = Path.of(pathString);
            File file = path.toFile();

            return OBJECT_MAPPER.readValue(file, Configuration.class);
        } catch (InvalidPathException e) {
            throw new ConfigurationLoadException("invalid path format: " + pathString, e);
        } catch (IOException e) {
            throw new ConfigurationLoadException("failed to read or parse configuration file: " + pathString, e);
        } catch (Exception e) {
            throw new ConfigurationLoadException("unexpected error loading configuration: " + pathString, e);
        }
    }
}
