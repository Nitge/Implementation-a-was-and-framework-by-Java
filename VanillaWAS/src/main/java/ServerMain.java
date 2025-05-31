package main.java;

import main.java.was.http.HttpServer;
import main.java.was.servlet.DispatcherServlet;
import main.java.was.servlet.ServletManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ServerMain {
    public static final int PORT = 80;

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(dispatcherServlet);
        new HttpServer(PORT, servletManager).start();
    }
}
