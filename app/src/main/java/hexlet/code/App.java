package hexlet.code;

import java.util.concurrent.Callable;
import org.rootdir.hexlet.java.m2k.DiffFormatJson;
import org.rootdir.hexlet.java.m2k.DiffFormatPlain;
import org.rootdir.hexlet.java.m2k.DiffFormatStylish;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 1.0",
        description = "Compares two configuration files and shows a difference.")
public class App implements Callable<Integer> {

    @Parameters(index = "0", paramLabel = "filepath1", description = "path to first file")
    private String left;

    @Parameters(index = "1", paramLabel = "filepath2", description = "path to second file")
    private String right;

    @Option(paramLabel = "format", defaultValue = "stylish", description = "output format [default: stylish]",
            names = {"-f", "--format"})
    private String outputFormat;

    @Option(paramLabel = "recursive", description = "recursively compare tree structure", names = {"-R", "--recursive"})
    private boolean recursive = false;

    public final String getGreeting() {
        return "Hello World!";
    }

    /**
     * Main application method.
     */
    @Override
    public final Integer call() throws Exception {
        var differ = Differ.fromPaths(left, right);
        differ = recursive ? differ.parseRecursive() : differ.parse();
        switch (this.outputFormat) {
            case "plain":
                System.out.print(DiffFormatPlain.format(differ.diff()));
                break;

            case "json":
                System.out.print(DiffFormatJson.format(differ.diff()));
                break;

            default:
                System.out.print(DiffFormatStylish.format(differ.diff()));
                break;
        }
        return 0;
    }

    /**
     *
     * @param args The command line arguments.
     */
    public static final void main(String[] args) {
        int exitCode = 0;
        try {
            exitCode = new CommandLine(new App()).execute(args);
        } catch (Exception e) {
            exitCode = 1;
        }

        System.exit(exitCode);
    }
}
