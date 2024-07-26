package com.spring.global.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LangCode {
    KO("ko", "한국어"),
    EN("en", "영어"),
    ZH("zh", "중국어");

    private final String code;
    private final String name;

    LangCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static LangCode fromCode(String code) {
        for (LangCode lang : LangCode.values()) {
            if (lang.getCode().equalsIgnoreCase(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown LangCode code: " + code);
    }

    public static List<Map<String, String>> toList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (LangCode lang : LangCode.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("code", lang.getCode());
            map.put("name", lang.getName());
            list.add(map);
        }
        return list;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}