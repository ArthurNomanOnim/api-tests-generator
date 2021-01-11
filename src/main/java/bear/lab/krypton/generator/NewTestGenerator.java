package bear.lab.krypton.generator;

import bear.lab.utils.*;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NewTestGenerator {

    private static Logger logger = LoggerFactory.getLogger(NewTestGenerator.class);

    public List<GeneratedCsvTest> generate(ApiOperation apiOperation) {

        List<GeneratedCsvTest> result = new ArrayList<>();

        switch (apiOperation.getMethod()) {
            case "GET":
                result.addAll(generateValidTestsForGet(apiOperation));
                result.addAll(generateInvalidTestsForGet(apiOperation));
                break;
            case "POST":
                result.addAll(generateValidTestsForPost(apiOperation));
                result.addAll(generateInvalidTestsForPost(apiOperation));
                break;
            case "PUT":
                result.addAll(generateValidTestsForPut(apiOperation));
                result.addAll(generateInvalidTestsForPut(apiOperation));
                break;
            case "PATCH":
                result.addAll(generateValidTestsForPatch(apiOperation));
                result.addAll(generateInvalidTestsForPatch(apiOperation));
                break;
            case "DELETE":
                result.addAll(generateValidTestsForDelete(apiOperation));
                result.addAll(generateInvalidTestsForDelete(apiOperation));
                break;
        }

        return result;
    }

    public List<GeneratedCsvTest> generateValidTestsForGet(ApiOperation apiOperation) {
        GeneratedCsvTest generatedCsvTest = getTest(apiOperation);

        generatedCsvTest.isValid = "true";
        generatedCsvTest.testType = "valid get test";
        generatedCsvTest.testData = "all params are valid";

        return Lists.newArrayList(generatedCsvTest);
    }

    public List<GeneratedCsvTest> generateValidTestsForPost(ApiOperation apiOperation) {
        GeneratedCsvTest generatedCsvTest = getTest(apiOperation);

        generatedCsvTest.isValid = "true";
        generatedCsvTest.testType = "valid post test";

        String msg = "all params are valid";

        if (apiOperation.getRef() != null) {
            msg += " json schema is " + apiOperation.getRef();
        }

        msg += " and execute GET to verify that object is created";

        generatedCsvTest.testData = msg;

        return Lists.newArrayList(generatedCsvTest);
    }

    public List<GeneratedCsvTest> generateValidTestsForPut(ApiOperation apiOperation) {
        GeneratedCsvTest generatedCsvTest = getTest(apiOperation);

        generatedCsvTest.isValid = "true";
        generatedCsvTest.testType = "valid put test";

        String msg = "all params are valid";

        if (apiOperation.getRef() != null) {
            msg += " json schema is " + apiOperation.getRef();
        }

        msg += " and execute GET to verify that object is updated";

        generatedCsvTest.testData = msg;

        return Lists.newArrayList(generatedCsvTest);
    }

    public List<GeneratedCsvTest> generateValidTestsForPatch(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();

        if (apiOperation.getRef() != null) {
            List<String> allFieldsFromSchema = JsonSchemaManager.getAllFieldsFromSchema(apiOperation.getRef());
            for (String s : allFieldsFromSchema) {
                GeneratedCsvTest generatedCsvTest = getTest(apiOperation);

                generatedCsvTest.isValid = "true";
                generatedCsvTest.testType = "valid patch test";
                generatedCsvTest.testData = "json is " + apiOperation.getRef() + " and change " + s + " field";
                result.add(generatedCsvTest);
            }
        }

        return result;
    }

    public List<GeneratedCsvTest> generateValidTestsForDelete(ApiOperation apiOperation) {
        GeneratedCsvTest generatedCsvTest = getTest(apiOperation);

        generatedCsvTest.isValid = "true";
        generatedCsvTest.testType = "valid delete test";
        generatedCsvTest.testData = "all params are valid and verify that object os deleted";

        return Lists.newArrayList(generatedCsvTest);
    }

    public List<GeneratedCsvTest> generateInvalidTestsForGet(ApiOperation apiOperation) {
        ArrayList<GeneratedCsvTest> generatedCsvTests = new ArrayList<>(invalidPath(apiOperation));
        logger.info("Operation {} - invalid path: {}", apiOperation.getOperationId(), generatedCsvTests.size());
        return generatedCsvTests;
    }

    public List<GeneratedCsvTest> generateInvalidTestsForPost(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();

        List<GeneratedCsvTest> generatedCsvTests = invalidPath(apiOperation);
        List<GeneratedCsvTest> generatedCsvTests1 = invalidBody(apiOperation);
        List<GeneratedCsvTest> generatedCsvTests2 = invalidSchema(apiOperation);

        logger.info("Operation {} - invalid path: {}, invalid body: {}, invalid schema {}",
                apiOperation.getOperationId(), generatedCsvTests.size(), generatedCsvTests1.size(), generatedCsvTests2.size());

        result.addAll(generatedCsvTests);
        result.addAll(generatedCsvTests1);
        result.addAll(generatedCsvTests2);

        return result;
    }

    public List<GeneratedCsvTest> generateInvalidTestsForPut(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();

        List<GeneratedCsvTest> generatedCsvTests = invalidPath(apiOperation);
        List<GeneratedCsvTest> generatedCsvTests1 = invalidBody(apiOperation);
        List<GeneratedCsvTest> generatedCsvTests2 = invalidSchema(apiOperation);

        logger.info("Operation {} - invalid path: {}, invalid body: {}, invalid schema {}",
                apiOperation.getOperationId(), generatedCsvTests.size(), generatedCsvTests1.size(), generatedCsvTests2.size());

        result.addAll(generatedCsvTests);
        result.addAll(generatedCsvTests1);
        result.addAll(generatedCsvTests2);

        return result;
    }

    public List<GeneratedCsvTest> generateInvalidTestsForPatch(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();

        List<GeneratedCsvTest> generatedCsvTests = invalidPath(apiOperation);
        List<GeneratedCsvTest> generatedCsvTests1 = invalidBody(apiOperation);
        List<GeneratedCsvTest> generatedCsvTests2 = invalidSchema(apiOperation);

        logger.info("Operation {} - invalid path: {}, invalid body: {}, invalid schema {}",
                apiOperation.getOperationId(), generatedCsvTests.size(), generatedCsvTests1.size(), generatedCsvTests2.size());

        result.addAll(generatedCsvTests);
        result.addAll(generatedCsvTests1);
        result.addAll(generatedCsvTests2);

        return result;
    }

    public List<GeneratedCsvTest> generateInvalidTestsForDelete(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();

        List<GeneratedCsvTest> generatedCsvTests = invalidPath(apiOperation);
        List<GeneratedCsvTest> generatedCsvTests1 = invalidBody(apiOperation);


        logger.info("Operation {} - invalid path: {}, invalid body: {},",
                apiOperation.getOperationId(), generatedCsvTests.size(), generatedCsvTests1.size());

        result.addAll(generatedCsvTests);
        result.addAll(generatedCsvTests1);

        return result;
    }

    public List<GeneratedCsvTest> invalidPath(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();
        List<String> pathParameters = apiOperation.getPathParameters();

        for (String parameter : pathParameters) {
            GeneratedCsvTest test = getTest(apiOperation);
            test.isValid = "false";
            test.testType = "invalid_path";
            test.testData = "parameter " + parameter + " has invalid value";
            result.add(test);
        }

        return result;
    }

    public List<GeneratedCsvTest> invalidBody(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();
        List<String> pathParameters = Lists.newArrayList("empty body", "invalid json");

        for (String parameter : pathParameters) {
            GeneratedCsvTest test = getTest(apiOperation);
            test.isValid = "false";
            test.testType = "invalid_body";
            test.testData = "body is " + parameter;
            result.add(test);
        }

        return result;
    }

    public List<GeneratedCsvTest> invalidSchema(ApiOperation apiOperation) {
        List<GeneratedCsvTest> result = new ArrayList<>();

        if (apiOperation.getRef() != null) {

            List<String> errors = Lists.newArrayList("has invalid value", "has null value", "is missing", "invalid type");

            for (String error : errors) {
                if (apiOperation.getRef() != null) {
                    List<String> allFieldsFromSchema = JsonSchemaManager.getAllFieldsFromSchema(apiOperation.getRef());

                    for (String parameter : allFieldsFromSchema) {
                        GeneratedCsvTest test = getTest(apiOperation);
                        test.isValid = "false";
                        test.testType = "invalid_schema";
                        test.testData = "json field " + parameter + " " + error;
                        result.add(test);
                    }
                }
            }


        }
        return result;
    }

    private GeneratedCsvTest getTest(ApiOperation apiOperation) {
        GeneratedCsvTest generatedCsvTest = new GeneratedCsvTest();
        generatedCsvTest.operation = apiOperation.getOperationId();
        generatedCsvTest.method = apiOperation.getMethod();
        generatedCsvTest.endpoint = apiOperation.getUrl();
        return generatedCsvTest;
    }
}
