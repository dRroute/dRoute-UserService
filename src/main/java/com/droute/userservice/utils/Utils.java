package com.droute.userservice.utils;

import java.util.Collection;
import java.util.Map;

public class Utils {

    /**
     * Returns the newValue only if it's not null or default, otherwise returns
     * oldValue.
     * Handles Strings, Numbers, Booleans, Collections, and Maps.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getUpdatedValue(T newValue, T oldValue) {
        if (newValue == null)
            return oldValue;

        if (newValue instanceof String str) {
            return (!str.trim().isEmpty()) ? newValue : oldValue;
        }

        if (newValue instanceof Number num) {
            return (num.doubleValue() != 0) ? newValue : oldValue;
        }

        if (newValue instanceof Boolean bool) {
            return bool ? newValue : oldValue;
        }

        if (newValue instanceof Collection<?> col) {
            return (!col.isEmpty()) ? newValue : oldValue;
        }

        if (newValue instanceof Map<?, ?> map) {
            return (!map.isEmpty()) ? newValue : oldValue;
        }

        // For all other object types (custom DTOs etc.)
        return newValue;
    }

}
