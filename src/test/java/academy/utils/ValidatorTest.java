package academy.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import academy.exceptions.ValidationException;
import academy.models.config.AffineParameters;
import academy.models.config.TransformationFunction;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ValidatorTest {

    @Test
    @DisplayName("Положительное целое число не должно бросать исключение")
    void validatePositiveInt_whenPositiveInteger_shouldNotThrow() {
        assertDoesNotThrow(() -> Validator.validatePositive(1));
    }

    @Test
    @DisplayName("Ноль должен бросить исключение")
    void validatePositiveInt_whenZero_shouldThrow() {
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.validatePositive(0));
        assertEquals("parameter must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Положительное десятичное число не должно бросать исключение")
    void validatePositiveDouble_whenPositiveDouble_shouldNotThrow() {
        assertDoesNotThrow(() -> Validator.validatePositive(5.1234));
    }

    @Test
    @DisplayName("Отрицательное целое число должно бросать исключение")
    void validatePositiveInteger_whenNegativeInteger_shouldThrow() {
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.validatePositive(-2));
        assertEquals("parameter must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Отрицательное десятичное число должно бросать исключение")
    void validatePositiveDouble_whenNegativeDouble_shouldThrow() {
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.validatePositive(-1.2));
        assertEquals("parameter must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Несуществующий путь должен проходить")
    void validatePath_whenNonExistentPath_shouldNotThrow(@TempDir Path tempDir) {
        Path nonExisting = tempDir.resolve("non_existing.png");
        assertDoesNotThrow(() -> Validator.validatePath(nonExisting.toString()));
    }

    @Test
    @DisplayName("Существующий путь должен бросать исключение")
    void validatePath_whenExistingFile_shouldThrow(@TempDir Path tempDir) throws IOException {
        Path existingFile = tempDir.resolve("existing.png");
        Files.createFile(existingFile);

        ValidationException exception =
                assertThrows(ValidationException.class, () -> Validator.validatePath(existingFile.toString()));
        assertEquals("output path already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Одиночный аффинный параметр в корректном формате должен спарситься")
    void validateAndGetAffineParams_whenSingleValid_shouldReturnObject() {
        String input = "<0.6>,<0.05>,<0.1>,<0.05>,<0.6>,<0.0>";

        List<AffineParameters> result = Validator.validateAndGetAffineParams(input);

        assertEquals(1, result.size());
        AffineParameters params = result.getFirst();
        assertEquals(0.6, params.a(), 0.001);
        assertEquals(0.05, params.b(), 0.001);
        assertEquals(0.0, params.f(), 0.001);
    }

    @Test
    @DisplayName("Несколько аффинных параметров в корректном формате через слэш должны спарситься")
    void validateAndGetAffineParams_whenMultipleValid_shouldReturnList() {
        String input = "<1.0>,<2.0>,<3.0>,<4.0>,<5.0>,<6.0>/<0.1>,<0.2>,<0.3>,<0.4>,<0.5>,<0.6>";

        List<AffineParameters> result = Validator.validateAndGetAffineParams(input);

        assertEquals(2, result.size());
        assertEquals(1.0, result.get(0).a(), 0.001);
        assertEquals(0.1, result.get(1).a(), 0.001);
    }

    @Test
    @DisplayName("Некорректный формат аффинных параметров должен бросать исключение")
    void validateAndGetAffineParams_whenInvalidFormat_shouldThrow() {
        String invalidInput = "1.0,2.0,3.0,4.0,5.0,6.0";

        ValidationException exception =
                assertThrows(ValidationException.class, () -> Validator.validateAndGetAffineParams(invalidInput));
        assertEquals("parameters passed in invalid format", exception.getMessage());
    }

    @Test
    @DisplayName("Одиночная функция в корректном формате должна спарситься")
    void validateAndGetFunctions_singleValid_shouldReturnCorrectObject() {
        String input = "linear:1.0";

        List<TransformationFunction> result = Validator.validateAndGetFunctions(input);

        assertEquals(1, result.size());
        assertEquals("linear", result.getFirst().name());
        assertEquals(1.0, result.getFirst().weight(), 0.001);
    }

    @Test
    @DisplayName("Несколько функций в корректном формате через запятую должны спарситься")
    void validateAndGetFunctions_multipleValid_shouldReturnList() {
        String input = "sinusoidal:1.5,linear:1.0,swirl:0.5";

        List<TransformationFunction> result = Validator.validateAndGetFunctions(input);

        assertEquals(3, result.size());
        assertEquals("sinusoidal", result.get(0).name());
        assertEquals("linear", result.get(1).name());
        assertEquals("swirl", result.get(2).name());
        assertEquals(0.5, result.get(2).weight(), 0.001);
    }

    @Test
    @DisplayName("Некорректный формат функций должен бросать исключение")
    void validateAndGetFunctions_invalidFormat_shouldThrow() {
        String invalidInput = "linear-1.0"; // Нет двоеточия

        ValidationException exception =
                assertThrows(ValidationException.class, () -> Validator.validateAndGetFunctions(invalidInput));
        assertEquals("parameters passed in invalid format", exception.getMessage());
    }
}
