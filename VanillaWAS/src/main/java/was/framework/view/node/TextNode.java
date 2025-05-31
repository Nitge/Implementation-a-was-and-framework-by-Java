package main.java.was.framework.view.node;

import main.java.was.framework.handler.adapter.Model;

public class TextNode implements Node{

    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public String render(Model model) {
        return text;
    }
}
