package academy.function;

public class FunctionFactory {

    public static Function getFunctionByName(String functionName) {
        return switch (functionName) {
            case "linear" -> new LinearFunction();
            default -> throw new IllegalArgumentException("");
        };
    }
}
