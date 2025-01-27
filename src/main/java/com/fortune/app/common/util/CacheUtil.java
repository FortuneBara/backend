package com.fortune.app.common.util;

public class CacheUtil {

    public static String getUserCacheKey(Long userId) {
        return userId.toString();
    }

    public static String getUserListCacheKey() {
        return "all";
    }
}
