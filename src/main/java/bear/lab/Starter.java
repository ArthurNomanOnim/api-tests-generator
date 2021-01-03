package bear.lab;

import bear.lab.utils.ApiOperation;
import bear.lab.utils.GeneratedTest;
import bear.lab.generators.CoreTestGenerator;
import bear.lab.utils.JsonSchemaManager;
import bear.lab.utils.SwaggerReader;
import bear.lab.utils.FeatureFileWriter;
import bear.lab.utils.TestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Starter {
    //b28e3cf6bbbfd868d532e8081cc998e0ec00f3dc

    static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws IOException {

        logger.info("Start processing");

        SwaggerReader swaggerReader = new SwaggerReader(TestConfiguration.openApiJsonUrl);

        String version = swaggerReader.getVersion();
        JsonSchemaManager.setSchemas(swaggerReader.getSchemas());
        List<ApiOperation> operations = swaggerReader.getOperations();

        CoreTestGenerator coreTestGenerator = new CoreTestGenerator();
        List<GeneratedTest> tests = operations.stream().map(coreTestGenerator::generateAllTests).flatMap(List::stream).collect(Collectors.toList());

        logger.info("Generated {} tests", tests.size());

        FeatureFileWriter featureFileWriter = new FeatureFileWriter();
        featureFileWriter.write(Paths.get(version + "_" + System.currentTimeMillis() + ".feature"), version, tests);
    }
}