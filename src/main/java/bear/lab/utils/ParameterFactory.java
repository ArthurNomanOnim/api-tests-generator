package bear.lab.utils;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class ParameterFactory {

    private final YamlReader yamlReader;

    public ParameterFactory() throws FileNotFoundException {
        yamlReader = new YamlReader(Paths.get("schemas", "valid_values.yml"));
    }

    public Object getValueFromYaml(String scheme, String key) {
        String fullPath = scheme == null ? key : scheme + "." + key;
        return yamlReader.getValue(fullPath);
    }

    public boolean isGeneratedValue(String scheme, String key) {
        Object valueFromYaml = getValueFromYaml(scheme, key);
        if (valueFromYaml == null) {
            return false;
        }

        if (valueFromYaml instanceof Map) {
            return ((Map) valueFromYaml).containsKey("generator");
        }

        return false;
    }

    public Object generateValue(String scheme, String key) {
        Object generatorName = getValueFromYaml(scheme, key);
        if (generatorName == null) {
            return null;
        }
        return generateValue(((Map) generatorName).get("generator").toString());
    }

    public Object generateValue(String factoryName) {
        switch (factoryName) {
            case "generateUUID":
                return UUID.randomUUID().toString();
            case "generatePetId":
                return createPetId();
            case "getOrderId":
                return 9;
            case "getCurrentTime":
                return getCurrentTime();
            default:
                return null;
        }
    }

    private String getCurrentTime() {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")).toString();
    }

    private int createPetId() {
        return 9;
    }

    public Object getOrGenerateValue(String scheme, String key) {
        if (isGeneratedValue(scheme, key)) {
            return generateValue(scheme, key);
        } else {
            return getValidValue(scheme, key);
        }
    }

    public Object getInvalidValue(String scheme, String key) {
        return "invalid_" + scheme + "_" + key + "_value";
    }

    public Object getValidValue(String key) {
        return getValueFromYaml(null, key);
    }

    public Object getValidValue(String scheme, String key) {
        return getValueFromYaml(scheme, key);
    }
}