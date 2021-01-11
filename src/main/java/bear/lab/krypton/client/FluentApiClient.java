package bear.lab.krypton.client;

import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FluentApiClient {

    private static final Logger logger = LoggerFactory.getLogger(FluentApiClient.class);
    private static final String baseUrl = "https://petstore.swagger.io/v2/";

    static {
        logger.info("Init API client");

        RestAssured.baseURI = baseUrl;
        RestAssured.filters((request, response, filterContext) -> {
            String requestMsg = "-->  Request " + request.getMethod() + " " + request.getURI();

            Response next = filterContext.next(request, response);
            String responseBody = ((RestAssuredResponseImpl) next).getBody().asString().replace("\n", " ").replace(" ", "");
            String responseMsg = "<-- Response " + next.getStatusCode() + " " + responseBody;

            logger.info(requestMsg);
            logger.info(responseMsg);

            return next;
        });
    }

    public RequestSpecification request() {
        return RestAssured.given()
                .accept(" application/json");
    }
}
