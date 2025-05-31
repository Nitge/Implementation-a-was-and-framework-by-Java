package main.java.was.framework.handler.adapter;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> map = new HashMap<>();

    public Model() {

    }

    public Model(Model model) {
        this.map.putAll(model.map);
    }

    public Object getAttribute(String key) {
        return map.get(key);
    }

    public void addAttribute(String key, Object value) {
        map.put(key, value);
    }
}
