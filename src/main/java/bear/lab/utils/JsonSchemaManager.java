package bear.lab.utils;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.swagger.oas.inflector.examples.ExampleBuilder;
import io.swagger.oas.inflector.examples.models.Example;
import io.swagger.oas.inflector.processors.JsonNodeExampleSerializer;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import net.minidev.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonSchemaManager {

    private static ParameterFactory parameterFactory;
    private static Map<String, Schema> schemas;

    static {
        try {
            parameterFactory = new ParameterFactory();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setSchemas(Map<String, Schema> schemas) {
        JsonSchemaManager.schemas = schemas;
    }

    public static Class<?> getParameterType(String ref, String param) {
        Object rawParam = schemas.get(ref).getProperties().get(param);

        if (rawParam instanceof IntegerSchema) {
            return Integer.class;
        }

        if (rawParam instanceof StringSchema) {
            return String.class;
        }

        if (rawParam instanceof ArraySchema) {
            Schema<?> items = ((ArraySchema) rawParam).getItems();
            if (items instanceof StringSchema) {
                return String.class;
            } else {
                return Integer.class;
            }
        }

        return null;
    }

    public static JSONObject generateJsonObject(String ref) {

        Example example = ExampleBuilder.fromSchema(schemas.get(ref), schemas);

        SimpleModule simpleModule = new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
        Json.mapper().registerModule(simpleModule);
        String jsonExample = Json.pretty(example);

        DocumentContext parse = JsonPath.parse(jsonExample);

        List<String> allFieldsFromSchema = getAllFieldsFromSchema(ref);

        for (String s : allFieldsFromSchema) {
            Object valueToInsert = parameterFactory.getOrGenerateValue(ref, s);
            if (parse.read(s) instanceof JSONArray) {
                parse.set(s, new JSONArray());
                parse.add(s, valueToInsert);
            } else {
                parse.set(s, valueToInsert);
            }

        }

        return new JSONObject(parse.jsonString());
    }


    public static List<String> getAllFieldsFromSchema(String ref) {
        return getAllFieldsFromSchema(ref, null);
    }

    private static List<String> getAllFieldsFromSchema(String ref, String prefix) {
        List<String> result = new ArrayList<>();

        Map<String, Schema> properties = schemas.get(ref).getProperties();

        for (Map.Entry<String, Schema> property : properties.entrySet()) {

            if (property.getValue().get$ref() != null) {
                String[] $refs = property.getValue().get$ref().split("/");
                result.addAll(getAllFieldsFromSchema($refs[$refs.length - 1], property.getKey()));
            } else {
                if (prefix == null) {
                    result.add(property.getKey());
                } else {
                    result.add(prefix + "." + property.getKey());
                }
            }
        }
        return result;
    }
}
