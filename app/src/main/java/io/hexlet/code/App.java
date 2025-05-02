package io.hexlet.code;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 1.0", description = "Compares two configuration files and shows a difference.")
public class App implements Callable<Integer> {

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
