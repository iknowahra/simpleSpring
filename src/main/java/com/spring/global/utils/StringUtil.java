package com.spring.global.utils;

import com.spring.global.error.ErrorCode;

public class StringUtil {

    private StringUtil() {
        throw new IllegalArgumentException(ErrorCode.NOT_AVAILABLE_CONSTRUCTOR.getMessage());
    }

    // Snake Case로 변환
    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(input.charAt(0)));

        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    // Camel Case로 변환
    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        boolean toUpperCase = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '_' || c == ' ' || c == '-') {
                toUpperCase = true;
            } else {
                if (toUpperCase) {
                    result.append(Character.toUpperCase(c));
                    toUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }

    // Pascal Case로 변환
    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String camelCase = toCamelCase(input);
        return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1);
    }
}
