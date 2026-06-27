package com.flin.online.function;

import java.util.HashMap;
public class Hardware extends HashMap<String, String>{
    public static final String NAME = "name";
    public static final String HINT_NAME = "hint";
    // Конструктор
    public Hardware(String name, String hint) {
        super.put(NAME, name);
        super.put(HINT_NAME, hint);
    }
}