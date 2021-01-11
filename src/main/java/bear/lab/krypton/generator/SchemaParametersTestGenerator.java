package bear.lab.krypton.generator;

import bear.lab.utils.ApiOperation;
import bear.lab.utils.GeneratedTest;
import bear.lab.utils.JsonSchemaManager;
import bear.lab.utils.ParameterFactory;

import java.util.ArrayList;
import java.util.List;

public class SchemaParametersTestGenerator implements ITestGenerator {

    private final ParameterFactory parameterFactory;

    public SchemaParametersTestGenerator(ParameterFactory parameterFactory) {
        this.parameterFactory = parameterFactory;
    }

    @Override
    public List<GeneratedTest> generate(ApiOperation apiOperation) {
        List<GeneratedTest> result = new ArrayList<>();
        String schema = apiOperation.getRef();

        if (schema != null) {

            String operationId = apiOperation.getOperationId();
            String method = apiOperation.getMethod();
            List<String> allFieldsFromTheRef = JsonSchemaManager.getAllFieldsFromSchema(schema);

            for (String field : allFieldsFromTheRef) {

                // for all methods we will check invalid values for each schema parameter
                GeneratedTest gt = new GeneratedTest(operationId);
                gt.givenBodyList.add("Request body schema is " + schema + ".json");
                gt.givenBodyList.add(field + " has value " + parameterFactory.getInvalidValue(schema, field));
                gt.then = "Response code should be 400";
                result.add(gt);


                // for PATCH we additional test all valid parameters
                if (method.equals("PATCH")) {

                    GeneratedTest gtValid = new GeneratedTest(operationId);

                    gtValid.givenBodyList.add("Request body schema is " + schema + ".json");

                    String givenBody = "";

                    if (parameterFactory.isGeneratedValue(schema, field)) {
                        givenBody += " is refer to the created " + field;
                    } else {
                        givenBody += " has value " + parameterFactory.getValidValue(schema, field);
                    }

                    gtValid.givenBodyList.add(givenBody);
                    gtValid.then = "Response code should be 400";
                    result.add(gtValid);
                }
            }
        }

        return result;
    }
}