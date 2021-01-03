package bear.lab.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Map;

public class YamlReader {

    static Logger logger = LoggerFactory.getLogger(YamlReader.class);
    private final Map<String, Object> yaml;

    public YamlReader(Path file) throws FileNotFoundException {
        logger.info("Read yml file: {}", file.toString());
        this.yaml = new Yaml().load(new FileInputStream(file.toFile()));
    }

    public Object getValueOrDefault(String key, Object defaultValue) {
        Object obj = getValue(key);
        return obj == null ? defaultValue : obj;
    }

    public boolean isPathExists(final String path) {
        return getValue(path) != null;
    }

    public Object getValue(final String path) {
        try {
            logger.info("Read value for the {} path", path);
            Map<String, Object> currentNode = yaml;

            String[] pathSegments = path.split("\\.");
            int lastArrayIndex = pathSegments.length - 1;
            for (int i = 0; i < lastArrayIndex; i++) {

                String key = pathSegments[i];

                if (currentNode.containsKey(key)) {
                    currentNode = (Map<String, Object>) currentNode.get(key);
                } else {
                    return null;
                }
            }

            String lastIndexKey = pathSegments[lastArrayIndex];

            Object obj = currentNode.get(lastIndexKey);

            if (obj == null) {
                return null;
            }

            if (obj.toString().startsWith("$ref:")) {
                String refKey = obj.toString().replace("$ref:", "");
                return getValue(refKey);
            } else {
                return obj;
            }

        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}