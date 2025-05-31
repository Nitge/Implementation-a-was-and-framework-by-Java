package main.java.was.framework.handler.mapping;

import java.lang.reflect.Method;

public class ControllerAndMethod  {
    private final Object controller;
    private final Method method;

    public ControllerAndMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}
