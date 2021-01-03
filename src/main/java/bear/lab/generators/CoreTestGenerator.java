package bear.lab.generators;

import bear.lab.utils.ApiOperation;
import bear.lab.utils.GeneratedTest;
import bear.lab.utils.ParameterFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoreTestGenerator {

    private final List<ITestGenerator> generators;
    private final DefaultTestGenerator defaultTestGenerator;

    public CoreTestGenerator() throws FileNotFoundException {
        ParameterFactory parameterFactory = new ParameterFactory();
        generators = new ArrayList<>();
        defaultTestGenerator = new DefaultTestGenerator(parameterFactory);
        generators.add(new InvalidPathTestGenerator(parameterFactory));
        generators.add(new InvalidBodyTestGenerator());
        generators.add(new SchemaParametersTestGenerator(parameterFactory));
    }

    public List<GeneratedTest> generateAllTests(ApiOperation apiOperation) {
        GeneratedTest baseTest = defaultTestGenerator.generate(apiOperation).get(0);

        List<GeneratedTest> result = generators
                .stream()
                .map(e -> e.generate(apiOperation))
                .flatMap(List::stream)
                .map(e -> merge(e, baseTest))
                .collect(Collectors.toList());

        result.add(baseTest);
        return result;
    }

    public GeneratedTest merge(GeneratedTest deltaTest, GeneratedTest baseTest) {

        //operation id
        deltaTest.operationId = baseTest.operationId;

        //given
        deltaTest.givenList.addAll(baseTest.givenList);

        //merge path
        for (String baseParam : baseTest.givenParamList) {
            for (String deltaParam : deltaTest.givenParamList) {
                if (!getParamName(baseParam).equals(getParamName(deltaParam))) {
                    deltaTest.givenParamList.add(baseParam);
                }
            }
        }

        //merge query
        if (deltaTest.givenBodyList.isEmpty()) {
            deltaTest.givenBodyList.addAll(baseTest.givenBodyList);
        }

        //when
        deltaTest.when = baseTest.when;

        return deltaTest;
    }

    private String getParamName(String str) {
        int i = str.indexOf("}");
        if (i == -1) {
            return "";
        }
        return str.substring(0, i + 1);
    }
}