package bear.lab.generators;

import bear.lab.utils.ApiOperation;
import bear.lab.utils.GeneratedTest;
import bear.lab.utils.ParameterFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultTestGenerator implements ITestGenerator {

    private final ParameterFactory parameterFactory;

    public DefaultTestGenerator(ParameterFactory parameterFactory) {
        this.parameterFactory = parameterFactory;
    }

    @Override
    public List<GeneratedTest> generate(ApiOperation apiOperation) {
        List<GeneratedTest> result = new ArrayList<>();

        GeneratedTest validTest = new GeneratedTest(apiOperation.getOperationId());

        // path
        for (String parameter : apiOperation.getPathParameters()) {
            String value = "{" + parameter + "}";

            if (parameterFactory.isGeneratedValue(null, parameter)) {
                value += " is refer to the created " + parameter;
            } else {
                value += " has value " + parameterFactory.getValidValue(parameter);
            }

            validTest.givenParamList.add(value);
        }

        // query
        for (String queryParameter : apiOperation.getQueryParameters()) {
            String validValue = "{" + queryParameter + "}";

            if (parameterFactory.isGeneratedValue(apiOperation.getRef(), queryParameter)) {
                validValue += " query parameter is refer to the created " + queryParameter;
            } else {
                validValue += " query parameter equals " + parameterFactory.getValidValue(queryParameter);
            }

            validTest.givenParamList.add(validValue);
        }

        //body
        if (apiOperation.getRef() != null) {
            validTest.givenBodyList.add("Request body schema is " + apiOperation.getRef() + ".json");
        }

        // generate 1 valid test
        validTest.when = String.format("Execute %s %s", apiOperation.getMethod(), apiOperation.getUrl());

        validTest.then = String.format("Response code should be %s", 200);
        result.add(validTest);

        return result;
    }
}