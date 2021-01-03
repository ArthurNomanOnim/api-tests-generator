package steps;

import bear.lab.utils.TestConfiguration;
import bear.lab.utils.JsonSchemaManager;
import bear.lab.utils.ParameterFactory;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class StepsForGenerator {

    private static final Logger logger = LoggerFactory.getLogger(StepsForGenerator.class);

    private String method;
    private String url;
    private String rawRequestBody;
    private String scheme;
    private JSONObject requestBody;

    private static OpenApiValidationFilter validationFilter;
    private static Map<String, Object> variables;
    private static ParameterFactory parameterFactory;
    private static Map<String, Object> queryParams;

    static {
        try {
            String swaggerJsonUrl = TestConfiguration.openApiJsonUrl;
            RestAssured.baseURI = TestConfiguration.host;

            JsonSchemaManager.setSchemas(new OpenAPIV3Parser().read(swaggerJsonUrl).getComponents().getSchemas());

            variables = new HashMap<>();
            queryParams = new HashMap<>();

            parameterFactory = new ParameterFactory();
            validationFilter = new OpenApiValidationFilter(swaggerJsonUrl);

            RestAssured.filters((request, response, filterContext) -> {
                String requestMsg = "-->  Request " + request.getMethod() + " " + request.getURI();

                Response next = filterContext.next(request, response);
                String responseBody = ((RestAssuredResponseImpl) next).getBody().asString().replace("\n", " ").replace(" ", "");
                String responseMsg = "<-- Response " + next.getStatusCode() + " " + responseBody;

                logger.info(requestMsg);
                logger.info(responseMsg);

                return next;
            });
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    @Before
    public void cleanVariables() {
        method = null;
        url = null;
        requestBody = null;
        rawRequestBody = null;
        scheme = null;

        variables.clear();
        queryParams.clear();
    }

    private String removeBrackets(String string) {
        logger.info("Remove brackets from the " + string);
        return string.replaceFirst("\\{", "").replace("}", "");
    }

    // -- GIVEN
    @Given("{} is refer to the created {}")
    public void createValueFromFactory(String variable, String factoryName) {
        logger.info("Set parameter {} from generator {}", variable, factoryName);

        String var = removeBrackets(variable);
        variables.put(var, parameterFactory.generateValue(scheme, factoryName));
    }

    @Given("{} has value {}")
    public void setValueForParameter(String parameter, Object value) {
        if (parameter.startsWith("{")) {
            logger.info("Set parameter {} to value {}", parameter, value);
            variables.put(removeBrackets(parameter), value);
        } else {
            logger.info("Set parameter {} to value {} in a json", parameter, value);
            requestBody = new JSONObject(JsonPath.parse(requestBody.toString()).set(parameter, value).jsonString());
        }
    }

    @Given("{} query parameter equals {}")
    public void setValueForQueryParam(String param, Object value) {
        logger.info("Set query {} to value {}", param, value);
        queryParams.put(removeBrackets(param), value);
    }

    @Given("Request body schema is {}")
    public void setRequestBody(String jsonName) {
        logger.info("Set request body to {} json", jsonName);
        scheme = jsonName;
        requestBody = JsonSchemaManager.generateJsonObject(jsonName.replace(".json", ""));
    }

    @Given("Request body is {}")
    public void setRequestBodyState(String state) {
        logger.info("Set request body state to {}", state);
        switch (state) {
            case "empty json":
                rawRequestBody = "{}";
                break;
            case "empty body":
                rawRequestBody = " ";
                break;
            case "invalid json schema body":
            case "invalid json":
                rawRequestBody = "{\"a\":b}";
                break;
        }
    }

    // -- WHEN

    @When("Execute {word} {}")
    public void setupRequest(String method, String url) {
        logger.info("Set request method {} and url {} ", method, url);
        this.method = method;
        this.url = url;
    }


    // -- THEN
    @Then("Response code should be {}")
    public void checkResponseCode(int code) {
        String body = "";
        if (rawRequestBody != null) {
            logger.info("Request body is taken from the rawString");
            body = rawRequestBody;
        } else if (requestBody != null) {
            logger.info("Request body is taken from the json body");
            body = requestBody.toString();
        }

        RequestSpecification request = RestAssured.given().filter(validationFilter);

        variables.forEach(request::pathParam);
        queryParams.forEach(request::queryParam);

        logger.info("Execute {} {} ", method, url);

        if (!body.isEmpty()) {
            logger.info("Request body: {}", body);
            request.contentType("application/json");
            request.body(body);
        }

        request.request(method.toUpperCase(), url).then().statusCode(code);
    }
}