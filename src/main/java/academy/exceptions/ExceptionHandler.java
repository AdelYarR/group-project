package academy.exceptions;

import java.io.PrintWriter;
import picocli.CommandLine;

/** Класс для перехвата исключений, чтобы не выводились строки-подсказки после сообщения исключения */
public class ExceptionHandler implements CommandLine.IParameterExceptionHandler {

    public int handleParseException(CommandLine.ParameterException ex, String[] args) {
        CommandLine cmd = ex.getCommandLine();
        PrintWriter err = cmd.getErr();

        err.println(ex.getMessage());

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : cmd.getCommandSpec().exitCodeOnInvalidInput();
    }
}
