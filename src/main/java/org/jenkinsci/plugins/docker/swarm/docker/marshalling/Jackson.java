
package org.jenkinsci.plugins.docker.swarm.docker.marshalling;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Jackson {

    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
    private static final ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());

    static {
        for (ObjectMapper objectMapper : Arrays.asList(defaultObjectMapper, yamlObjectMapper)) {
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }



  private static String toJSON(ObjectMapper mapper, Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Cannot marshal to JSON: " + object, e);
    }
  }

  private static <T> T fromJSON(ObjectMapper mapper, String json, Class<T> expectedType) {
    try {
      return mapper.readerFor(expectedType).readValue(json);
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot unmarshal JSON as " + expectedType.getSimpleName(), e);
    }
  }
    public static <T> T fromJSON(String json, Class<T> expectedType) {
      return fromJSON(defaultObjectMapper,json,expectedType);
    }

  private static <T> T fromJSONArray(ObjectMapper mapper, String json, Class<T> expectedType) {
    try {
      CollectionType arrayType = mapper.getTypeFactory()
              .constructCollectionType(List.class, expectedType);
      return mapper.readerFor(arrayType).readValue(json);
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot unmarshal JSON as " + expectedType.getSimpleName(), e);
    }
  }



  public static String toJson(Object object){
      return toJSON(getDefaultObjectMapper(),object);

  }
  public static ObjectMapper getDefaultObjectMapper() {
    return defaultObjectMapper;
  }

  public static ObjectMapper getYamlObjectMapper() {
    return yamlObjectMapper;
  }

    public static Object fromJSON(String json, Class<?> responseClass, ResponseType responseType) {
        if(responseType == ResponseType.CLASS){
            return fromJSON(json,responseClass);
        }
        return fromJSONArray(defaultObjectMapper,json,responseClass);
    }
}
