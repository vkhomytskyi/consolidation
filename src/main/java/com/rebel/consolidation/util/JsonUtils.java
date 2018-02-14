package com.rebel.consolidation.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper() {{
        registerModule(new Jdk8Module());
        registerModule(new ParameterNamesModule());
        registerModule(new JavaTimeModule());
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }};

    private static String objectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot transform object:" + object + " to json", ex);
        }
    }

    public static <T> T fromJson(String json, Class<T> classForType) {
        return fromJson(new JsonObject(json), classForType);
    }

    public static <T> T fromJson(JsonObject json, Class<T> classForType) {
        return readValue(json.encode(), classForType);
    }

    private static <T> T readValue(String source, Class<T> classForType) {
        try {
            return mapper.readValue(source, classForType);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot transform json:\n" + source + "\n to object of type : " + classForType.getName(), ex);
        }
    }

    public static JsonObject toJson(Object o) {
        try {
            return new JsonObject(mapper.convertValue(o, Map.class));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot transform object " + o.getClass() + " to Map", e);
        }
    }

    public static JsonArray toJson(Collection o) {
        try {
            return new JsonArray(objectToJson(o));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot transform object " + o.getClass() + " to Map", e);
        }
    }
}
