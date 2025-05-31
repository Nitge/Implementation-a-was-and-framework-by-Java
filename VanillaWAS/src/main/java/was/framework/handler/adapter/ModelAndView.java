package main.java.was.framework.handler.adapter;

public class ModelAndView {
    private Model model;
    private String viewName;

    public ModelAndView() {
    }

    public ModelAndView(Model model, String viewName) {
        this.model = model;
        this.viewName = viewName;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
