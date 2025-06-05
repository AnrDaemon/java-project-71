package io.hexlet.code;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 1.0", description = "Compares two configuration files and shows a difference.")
public class App implements Callable<Integer> {

    @Parameters(index = "0", paramLabel = "filepath1", description = "path to first file")
    private String left;

    @Parameters(index = "1", paramLabel = "filepath2", description = "path to second file")
    private String right;

    @Option(paramLabel = "format", defaultValue = "stylish", description = "output format [default: stylish]", names = {
            "-f", "--format" })
    private String outputFormat;

    public String getGreeting() {
        return "Hello World!";
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        System.out.println(this.getGreeting());
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
