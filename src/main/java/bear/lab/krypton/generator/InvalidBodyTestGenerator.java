package bear.lab.krypton.generator;

import bear.lab.utils.ApiOperation;
import bear.lab.utils.GeneratedTest;

import java.util.ArrayList;
import java.util.List;

public class InvalidBodyTestGenerator implements ITestGenerator {
    @Override
    public List<GeneratedTest> generate(ApiOperation apiOperation) {
        List<GeneratedTest> result = new ArrayList<>();

        if (apiOperation.getRef() == null) {
            return result;
        }

        String scheme = apiOperation.getRef();

        // GENERATE 3 INVALID TESTS
        result.add(generatedInvalidBodyTest(scheme, "empty json"));
        result.add(generatedInvalidBodyTest(scheme, "invalid json"));
        result.add(generatedInvalidBodyTest(scheme, "invalid schema body"));
        return result;
    }

    private GeneratedTest generatedInvalidBodyTest(String operationId, String bodyType) {
        GeneratedTest gt = new GeneratedTest(operationId);
        gt.givenBodyList.add("Request body is " + bodyType);
        gt.then = "Response code should be 400";
        return gt;
    }
}
