package bear.lab.krypton.generator;

import bear.lab.utils.ApiOperation;
import bear.lab.utils.GeneratedTest;

import java.util.List;

public interface ITestGenerator {

    List<GeneratedTest> generate(ApiOperation apiOperation);
}
