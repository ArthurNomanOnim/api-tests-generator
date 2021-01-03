package bear.lab.utils;

import bear.lab.utils.ApiOperation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.util.*;
import java.util.stream.Collectors;


public class SwaggerReader {

    private final OpenAPI openAPI;

    public SwaggerReader(String url) {
        openAPI = new OpenAPIV3Parser().read(url);
    }

    public String getVersion() {
        return openAPI.getInfo().getVersion();
    }

    public String getBaseEndpoint() {
        return openAPI.getServers().get(0).getUrl();
    }

    public Map<String, Schema> getSchemas() {
        return openAPI.getComponents().getSchemas();
    }

    public List<ApiOperation> getOperations() {
        Set<Map.Entry<String, PathItem>> paths = openAPI.getPaths().entrySet();
        return paths.stream().map(e -> getApiOperation(e.getValue(), e.getKey())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static ApiOperation getApiOperation(PathItem pathItem, String pathKey) {
        if (pathItem.getGet() != null) {
            if (pathItem.getGet().getDeprecated() == null) {
                return new ApiOperation(pathItem.getGet(), pathKey, "GET");
            }
        }

        if (pathItem.getPost() != null) {
            if (pathItem.getPost().getDeprecated() == null) {
                return new ApiOperation(pathItem.getPost(), pathKey, "POST");
            }
        }

        if (pathItem.getPut() != null) {
            if (pathItem.getPut().getDeprecated() == null) {
                return new ApiOperation(pathItem.getPut(), pathKey, "PUT");
            }
        }

        if (pathItem.getPatch() != null) {
            if (pathItem.getPatch().getDeprecated() == null) {
                return new ApiOperation(pathItem.getPatch(), pathKey, "PATCH");
            }
        }

        if (pathItem.getDelete() != null) {
            if (pathItem.getDelete().getDeprecated() != null) {
                return new ApiOperation(pathItem.getDelete(), pathKey, "DELETE");
            }
        }

        return null;
    }
}