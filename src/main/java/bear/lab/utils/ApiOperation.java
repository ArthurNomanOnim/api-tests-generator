package bear.lab.utils;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ApiOperation {

    private static final String APPLICATION_JSON = "application/json";
    private final List<String> pathParameters;
    private final String operationId;
    private final String method;
    private final String url;
    private String ref;
    private final List<String> queryParameters;

    public ApiOperation(Operation operation, String url, String method) {
        this.method = method;
        this.operationId = operation.getOperationId();
        this.url = url;

        List<Parameter> parameters = operation.getParameters();

        if (parameters != null) {
            pathParameters = parameters.stream()
                    .filter(e -> e.getIn().equals("path"))
                    .map(Parameter::getName)
                    .collect(Collectors.toList());

            queryParameters = parameters.stream()
                    .filter(e -> e.getIn().equals("query"))
                    .map(Parameter::getName)
                    .collect(Collectors.toList());
        } else {
            pathParameters = new ArrayList<>();
            queryParameters = new ArrayList<>();
        }

        this.ref = null;

        if (operation.getRequestBody() != null && operation.getRequestBody().getContent() != null) {

            MediaType mediaType = operation.getRequestBody().getContent().get(APPLICATION_JSON);

            if (mediaType != null) {
                String tmpRef;
                Schema<?> schema = mediaType.getSchema();
                if ("array".equals(schema.getType())) {
                    tmpRef = ((ArraySchema) schema).getItems().get$ref();
                } else {
                    tmpRef = schema.get$ref();
                }

                if (tmpRef != null) {
                    String[] split = tmpRef.split("/");
                    this.ref = split[split.length - 1];
                }
            }
        }
    }

    public String getOperationId() {
        return operationId;
    }

    public List<String> getQueryParameters() {
        return queryParameters;
    }

    public List<String> getPathParameters() {
        return pathParameters;
    }

    public String getRef() {
        return ref;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }
}
