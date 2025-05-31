package main.java.was.framework.view.node;

import main.java.was.framework.handler.adapter.Model;

public class VariableNode implements Node {

    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public String render(Model model) {
        Object value = model.getAttribute(name);
        if(value != null) {
            return value.toString();
        } else {
            return "";
        }
    }
}
