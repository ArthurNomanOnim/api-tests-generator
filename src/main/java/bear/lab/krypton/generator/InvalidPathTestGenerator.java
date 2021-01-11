package bear.lab.krypton.generator;

import bear.lab.utils.ApiOperation;
import bear.lab.utils.GeneratedTest;
import bear.lab.utils.ParameterFactory;

import java.util.ArrayList;
import java.util.List;

public class InvalidPathTestGenerator implements ITestGenerator {

    private final ParameterFactory parameterFactory;

    public InvalidPathTestGenerator(ParameterFactory parameterFactory) {
        this.parameterFactory = parameterFactory;
    }

    @Override
    public List<GeneratedTest> generate(ApiOperation apiOperation) {
        List<String> pathParameters = apiOperation.getPathParameters();
        List<GeneratedTest> result = new ArrayList<>();

        // generate invalid test for each parameter
        for (String parameter : pathParameters) {
            String value = "{" + parameter + "} has value " + parameterFactory.getInvalidValue(apiOperation.getRef(), parameter);
            GeneratedTest gt = new GeneratedTest(apiOperation.getOperationId());
            gt.givenParamList.add(value);
            gt.then = "Response code should be 404";
            result.add(gt);
        }

        return result;
    }

}