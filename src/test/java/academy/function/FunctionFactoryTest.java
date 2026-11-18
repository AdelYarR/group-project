package academy.function;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FunctionFactoryTest {

    @ParameterizedTest
    @ValueSource(strings = {"linear", "sinusoidal", "spherical", "swirl", "horseshoe"})
    @DisplayName("Фабрика должна корректно генерировать объект по названию функции")
    void getFunctionByName_whenAllSupportedFunctions_shouldReturnObject(String functionName) {
        assertDoesNotThrow(() -> {
            Function function = FunctionFactory.getFunctionByName(functionName);
            assertNotNull(function);
        });
    }

    @Test
    @DisplayName("Несуществующая функция должна бросать исключение")
    void getFunctionByName_whenUnknownName_shouldThrowException() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> FunctionFactory.getFunctionByName("unknown"));
        assertTrue(exception.getMessage().contains("doesn't exist"));
    }

    @Test
    @DisplayName("Null должен бросать исключение")
    void getFunctionByName_whenNull_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> FunctionFactory.getFunctionByName(null));
    }

    @Test
    @DisplayName("Пустая строка должна бросать исключение")
    void getFunctionByName_whenEmptyString_shouldThrowException() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> FunctionFactory.getFunctionByName(""));
        assertTrue(exception.getMessage().contains("doesn't exist"));
    }
}
