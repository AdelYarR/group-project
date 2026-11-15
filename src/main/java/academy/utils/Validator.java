package academy.utils;

import academy.exceptions.ValidationException;
import academy.models.AffineParameters;
import academy.models.TransformationFunction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Валидатор всех параметров, передаваемых в командной строке консоли при запуске программы. */
public class Validator {

    private Validator() {}

    public static void validatePositive(int n) {
        if (n <= 0) {
            throw new ValidationException("");
        }
    }

    public static void validatePositive(double n) {
        if (n <= 0) {
            throw new ValidationException("");
        }
    }

    public static void validatePath(String pathString) {
        Path path = Path.of(pathString);

        if (Files.exists(path)) {
            throw new ValidationException("");
        }
    }

    public static List<AffineParameters> validateAndGetAffineParams(String affineParametersString) {
        List<AffineParameters> affineParametersList = new ArrayList<>();

        String regex = "^<([\\d.]+)>,<([\\d.]+)>,<([\\d.]+)>,<([\\d.]+)>,<([\\d.]+)>,<([\\d.]+)>$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        String[] parts = affineParametersString.split("/");
        for (String part : parts) {
            matcher = pattern.matcher(part);

            if (!matcher.matches()) {
                throw new ValidationException("");
            }

            AffineParameters affineParameters = new AffineParameters(
                Double.parseDouble(matcher.group(1)),
                Double.parseDouble(matcher.group(2)),
                Double.parseDouble(matcher.group(3)),
                Double.parseDouble(matcher.group(4)),
                Double.parseDouble(matcher.group(5)),
                Double.parseDouble(matcher.group(6))
            );

            affineParametersList.add(affineParameters);
        }

        return affineParametersList;
    }

    public static List<TransformationFunction> validateAndGetFunctions(String functionsString) {
        List<TransformationFunction> functions = new ArrayList<>();

        String regex = "^([a-z]+):([\\d.]+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        String[] parts = functionsString.split(",");
        for (String part : parts) {
            matcher = pattern.matcher(part);

            if (!matcher.matches()) {
                throw new ValidationException("");
            }

            TransformationFunction function = new TransformationFunction(
                matcher.group(1),
                Double.parseDouble(matcher.group(2))
            );

            functions.add(function);
        }

        return functions;
    }
}
