package bear.lab;

import bear.lab.krypton.generator.NewTestGenerator;
import bear.lab.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class Starter {

    static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws IOException {

        logger.info("Start processing");

        String swaggerUrl = "";

        SwaggerReader swaggerReader = new SwaggerReader(swaggerUrl);

        String version = swaggerReader.getVersion();
        JsonSchemaManager.setSchemas(swaggerReader.getSchemas());
        List<ApiOperation> operations = swaggerReader.getOperations();

        logger.info("Collect {} operations", operations.size());

        NewTestGenerator newTestGenerator = new NewTestGenerator();

        List<GeneratedCsvTest> collect = operations.stream().map(newTestGenerator::generate).flatMap(List::stream).collect(Collectors.toList());

        logger.info("Generated {} tests", collect.size());

        System.out.println("!");

        FileWriter fileWriter = new FileWriter(new File("output.csv"));
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("operation, endpoint, method, isValid, test_type, test_data");
        String tmp = "%s,%s,%s,%s,%s,%s";
        collect.forEach(e -> printWriter.println(String.format(tmp, e.operation, e.endpoint, e.method, e.isValid, e.testType, e.testData)));
        printWriter.close();
    }
}