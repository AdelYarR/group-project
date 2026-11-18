package academy.function;

import academy.function.impl.HorseshoeFunction;
import academy.function.impl.LinearFunction;
import academy.function.impl.SinusoidalFunction;
import academy.function.impl.SphericalFunction;
import academy.function.impl.SwirlFunction;

public class FunctionFactory {

    public static Function getFunctionByName(String functionName) {
        return switch (functionName) {
            case "linear" -> new LinearFunction();
            case "sinusoidal" -> new SinusoidalFunction();
            case "spherical" -> new SphericalFunction();
            case "swirl" -> new SwirlFunction();
            case "horseshoe" -> new HorseshoeFunction();
            default -> throw new IllegalArgumentException(functionName + " function doesn't exist");
        };
    }
}
