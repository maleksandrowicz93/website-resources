package com.github.maleksandrowicz93.websiteresources.utils;

/**
 * This class contains constants and methods related to application API.
 */
public class ApiUtils {

    public static final String BASE_PATH = "/website";

    private ApiUtils() {}

    public static String getSpecifiedPath() {
        return getSpecifiedPath("1");
    }

    public static String getSpecifiedPath(String id) {
        return BASE_PATH + "/" + id;
    }
}
