package main.java.was.servlet;

import main.java.was.http.HttpRequest;
import main.java.was.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class ServletManager {

    private final Map<String, HttpServlet> servletMap = new HashMap<>();
    private HttpServlet defaultServlet;

    public void add(String path, HttpServlet servlet) {
        servletMap.put(path, servlet);
    }

    public void setDefaultServlet(HttpServlet defaultServlet) {
        this.defaultServlet = defaultServlet;
    }

    public void execute(HttpRequest request, HttpResponse response) {
        try {
            HttpServlet servlet = servletMap.getOrDefault(request.getPath(), defaultServlet);
            servlet.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
