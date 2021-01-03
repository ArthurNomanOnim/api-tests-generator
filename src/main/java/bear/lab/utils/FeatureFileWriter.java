package bear.lab.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class FeatureFileWriter {

    private static final String DOUBLE_SPACE = "  ";

    public void write(Path file, String version, List<GeneratedTest> scenarios) throws IOException {
        FileWriter fileWriter = new FileWriter(file.toFile());
        PrintWriter printWriter = new PrintWriter(fileWriter);

        // API INFO
        printWriter.println("#version: " + version);
        printWriter.println("#generated: " + LocalDateTime.now().toString());
        printWriter.println();

        // FEATURE block
        printWriter.println("Feature: Generated tests");
        printWriter.println();
        printWriter.println();

        // Init tests block
//        printWriter.println(DOUBLE_SPACE + "Scenario: Tests is initialized");
//        printWriter.println(DOUBLE_SPACE + DOUBLE_SPACE + "Given Tests is initialized");
//        printWriter.println();
//        printWriter.println();

        // TESTS block
        for (GeneratedTest generatedTest : scenarios) {
            String when = generatedTest.when;
            String then = generatedTest.then;

            String scenario = when + " " + then.substring(0, 1).toLowerCase() + then.substring(1);

            printWriter.println(DOUBLE_SPACE + "@" + generatedTest.operationId);
            printWriter.println(DOUBLE_SPACE + "Scenario: " + scenario);

            generatedTest.givenList.addAll(generatedTest.givenParamList);
            generatedTest.givenList.addAll(generatedTest.givenBodyList);

            if (!generatedTest.givenList.isEmpty() && !generatedTest.givenList.get(0).isEmpty()) {
                printWriter.println(DOUBLE_SPACE + DOUBLE_SPACE + "Given " + generatedTest.givenList.get(0));
            }

            for (int i = 1; i < generatedTest.givenList.size(); i++) {
                if (generatedTest.givenList.get(i) != null && !generatedTest.givenList.get(i).isEmpty()) {
                    printWriter.println(DOUBLE_SPACE + DOUBLE_SPACE + "And " + generatedTest.givenList.get(i));
                }
            }

            printWriter.println(DOUBLE_SPACE + DOUBLE_SPACE + "When " + when);
            printWriter.println(DOUBLE_SPACE + DOUBLE_SPACE + "Then " + then);
            printWriter.println();
        }

        printWriter.close();
    }
}