package com.example.bootopen.common.utils.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.Map;


public class GsonUtils {
    private static Gson gson = null;
    private static Gson gsonWithoutAnnotation = null;

    static {
        gson = new GsonBuilder()
                //在序列化或反序列化时 排除 所有 没有被 @Expose 注解的字段。
                .excludeFieldsWithoutExposeAnnotation()
                .enableComplexMapKeySerialization().serializeNulls().disableHtmlEscaping()
                .setDateFormat("yyyyMMddHHmmss")
                .setVersion(1.00)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        gsonWithoutAnnotation = new GsonBuilder().enableComplexMapKeySerialization().setDateFormat("yyyyMMddHHmmss").setVersion(1.00)
                .disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    }

    public static String toJsonAnn(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return gson.toJson(obj);
    }

    public static String toJson(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return gsonWithoutAnnotation.toJson(obj);
    }

    public static <V> V fromJson(String str, Class<V> cls) {
        return gsonWithoutAnnotation.fromJson(str, cls);
    }

    /**
     *
     * GsonUtils.toType(body, new TypeToken<RiskBaseRespDto<RiskVerifyRespDto>>() {}.getType())
     * @param str
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T toType(String str, Type type) {
        return gsonWithoutAnnotation.fromJson(str, type);
    }

    @SneakyThrows
    public static Map<String, Object> toMap(Object obj) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gsonWithoutAnnotation.fromJson(toJson(obj), type);
    }

    @SneakyThrows(JsonSyntaxException.class)
    public static Map<String, String> toStrMap(Object obj) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return gsonWithoutAnnotation.fromJson(toJson(obj), type);
    }

    public static JsonObject toJsonObject(Object obj) {
        try {
            JsonParser parser = new JsonParser();
            return (JsonObject)parser.parse(toJson(obj));
        }catch (Exception e){

        }
        return null;
    }

}
