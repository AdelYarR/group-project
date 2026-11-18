package academy.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.exceptions.ConfigurationLoadException;
import academy.models.config.Configuration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfigLoadTest {

    @Test
    @DisplayName("Валидный конфиг файл, успешная загрузка")
    void loadFromJson_whenValidConfig_shouldLoadSuccessfully(@TempDir Path tempDir) throws IOException {
        String jsonConfig =
                """
            {
              "size": {
                "width": 1920,
                "height": 1080
              },
              "seed": 42.5,
              "iteration_count": 1000000,
              "output_path": "result.png",
              "threads": 4,
              "affine_params": [
                {"a": 0.6, "b": -0.05, "c": -0.1, "d": 0.05, "e": 0.6, "f": 0.0}
              ],
              "functions": [
                {"name": "linear", "weight": 1.0}
              ],
              "gamma_correction": 2.2,
              "symmetry_level": 1
            }
            """;

        File configFile = tempDir.resolve("config.json").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(jsonConfig);
        }

        Configuration config = ConfigLoad.loadFromJson(configFile.getAbsolutePath());

        assertNotNull(config);
        assertEquals(1920, config.getWidth());
        assertEquals(1080, config.getHeight());
        assertEquals(42.5, config.getSeed(), 0.001);
        assertEquals(1000000, config.getIterationCount());
        assertEquals(1, config.getSymmetryLevel());
    }

    @Test
    @DisplayName("Минимальный конфиг, загружается с дефолтами")
    void loadFromJson_whenMinimalConfig_shouldLoadWithDefaults(@TempDir Path tempDir) throws IOException {
        String minimalJson =
                """
            {
              "affine_params": [
                {"a": 1.0, "b": 0.0, "c": 0.0, "d": 0.0, "e": 1.0, "f": 0.0}
              ],
              "functions": [
                {"name": "linear", "weight": 1.0}
              ]
            }
            """;

        File configFile = tempDir.resolve("minimal.json").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(minimalJson);
        }

        Configuration config = ConfigLoad.loadFromJson(configFile.getAbsolutePath());

        assertNotNull(config);
        assertTrue(config.getWidth() > 0);
        assertTrue(config.getHeight() > 0);
        assertEquals(1, config.getThreadCount());
    }

    @Test
    @DisplayName("Несуществующий файл должен бросать IOException")
    void loadFromJson_whenNonExistentFile_throwsIOException() {
        assertThrows(ConfigurationLoadException.class, () -> ConfigLoad.loadFromJson("non_existing.json"));
    }
}
