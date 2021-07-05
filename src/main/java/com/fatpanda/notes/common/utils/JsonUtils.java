package com.fatpanda.notes.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @Author xyy
 * @Date 2021/1/29
 */
public class JsonUtils {

    private static ObjectMapper objectMapper;

    private static ObjectMapper objectMapperWithAnnotation;

    private static ObjectMapper initObjectMapper(ObjectMapper objectMapper) {
        if (null == objectMapper) {
            objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            //日期序列化
            // javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            //javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

            //日期反序列化
            // javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            //javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

            objectMapper.registerModule(javaTimeModule);
        }
        return objectMapper;
    }

    /**
     * 对象转Json
     *
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        objectMapper = initObjectMapper(objectMapper);
        String jsonStr = objectMapper.writeValueAsString(obj);
        return jsonStr;
    }

    /**
     * 解析json
     *
     * @param content   json字符串
     * @param valueType 字符串的类型
     * @return 对象
     */
    public static <T> T fromJson(String content, Class<T> valueType) {
        objectMapper = initObjectMapper(objectMapper);
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws JsonProcessingException {
    }


}
