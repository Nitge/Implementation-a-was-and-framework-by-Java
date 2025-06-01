package main.java;

import main.java.was.http.HttpServer;
import main.java.was.servlet.DispatcherServlet;
import main.java.was.servlet.ServletManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ServerMain {
    public static int PORT = 12345;

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(dispatcherServlet);
        if(args.length > 0) {
            PORT = Integer.parseInt(args[0]);
        }
        new HttpServer(PORT, servletManager).start();
    }
}
