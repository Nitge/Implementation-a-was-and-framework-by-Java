package main.java.was.http;



import main.java.was.servlet.ServletManager;

import java.io.*;
import java.net.Socket;

import static main.java.util.Logger.log;


public class HttpRequestHandler implements Runnable{

    private final Socket socket;
    private final ServletManager servletManager;

    public HttpRequestHandler(Socket socket, ServletManager servletManager) {
        this.socket = socket;
        this.servletManager = servletManager;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (IOException e) {
            log(e);
        }
    }

    private void process() throws IOException {
        try(socket;
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream()){

            String hostAddress = socket.getInetAddress().getHostAddress();
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            log(hostAddress + " --> HTTP 요청: " + request);
            servletManager.execute(request, response);

            response.flush();
            log(hostAddress + " <-- HTTP 응답: " + response);
        }
    }


}
