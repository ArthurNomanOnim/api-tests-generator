package bear.lab.krypton.client;

import io.restassured.response.Response;

public class ApiResponse<T> {

    private int code;
    private final Response response;
    private final Class<T> responseClass;

    public ApiResponse(Response response, Class<T> responseClass) {
        this.response = response;
        this.responseClass = responseClass;
    }

    public String getBody() {
        return response.getBody().asString();
    }

    public T asObject() {
        return response.getBody().as(responseClass);
    }

    public int getCode() {
        return response.statusCode();
    }

    public Response getResponse() {
        return response;
    }
}
