package bear.lab.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        return paths.stream().map(e -> getApiOperation(e.getValue(), e.getKey())).flatMap(List::stream).collect(Collectors.toList());
    }

    private static List<ApiOperation> getApiOperation(PathItem pathItem, String pathKey) {

        List<ApiOperation> result = new ArrayList<>();

        if (pathItem.getGet() != null) {
            if (pathItem.getGet().getDeprecated() == null || !pathItem.getGet().getDeprecated()) {
                result.add(new ApiOperation(pathItem.getGet(), pathKey, "GET"));
            }
        }

        if (pathItem.getPost() != null) {
            if (pathItem.getPost().getDeprecated() == null || !pathItem.getPost().getDeprecated()) {
                result.add(new ApiOperation(pathItem.getPost(), pathKey, "POST"));
            }
        }

        if (pathItem.getPut() != null) {
            if (pathItem.getPut().getDeprecated() == null || !pathItem.getPut().getDeprecated()) {
                result.add(new ApiOperation(pathItem.getPut(), pathKey, "PUT"));
            }
        }

        if (pathItem.getPatch() != null) {
            if (pathItem.getPatch().getDeprecated() == null || !pathItem.getPatch().getDeprecated()) {
                result.add(new ApiOperation(pathItem.getPatch(), pathKey, "PATCH"));
            }
        }

        if (pathItem.getDelete() != null) {
            if (pathItem.getDelete().getDeprecated() != null || !pathItem.getDelete().getDeprecated()) {
                result.add(new ApiOperation(pathItem.getDelete(), pathKey, "DELETE"));
            }
        }

        return result;
    }
}