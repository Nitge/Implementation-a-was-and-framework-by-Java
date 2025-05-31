package main.java.was.servlet;

import main.java.was.http.HttpRequest;
import main.java.was.http.HttpResponse;

import java.io.IOException;

public interface HttpServlet {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
