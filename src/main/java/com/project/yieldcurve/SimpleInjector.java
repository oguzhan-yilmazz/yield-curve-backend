package com.project.yieldcurve;

public class SimpleInjector {
    private static JsonParser jsonParser = new JsonParser();
    private static DataMapper dataMapper = new DataMapper();

    public static JsonParser getJsonParser() {
        return jsonParser;
    }

    public static DataMapper getDataMapper() {
        return dataMapper;
    }
}
