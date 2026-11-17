package academy.utils;

import academy.models.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class ConfigLoad {

    private ConfigLoad() {}

    public static Configuration loadFromJson(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Configuration config = objectMapper.readValue(new File(path), Configuration.class);
        return config;
    }
}
