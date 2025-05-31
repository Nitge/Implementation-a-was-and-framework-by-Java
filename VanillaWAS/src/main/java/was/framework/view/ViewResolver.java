package main.java.was.framework.view;

public class ViewResolver {

    public View resolveView(String viewName) {
        return new View(viewName);
    }
}
