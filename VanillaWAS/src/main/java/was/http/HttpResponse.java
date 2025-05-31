package main.java.was.http;

import main.java.was.http.header.ContentType;
import main.java.was.http.header.HttpStatus;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpResponse {

    private HttpStatus httpStatus = HttpStatus.OK;
    private final Map<String, String> headers = new HashMap<>();

    private PrintWriter writer;
    private final OutputStream outputStream;
    private final ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream(); // Http 본문

    // flush 중복 호출 방지용
    private boolean committed = false;

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setContentType(ContentType contentType) {
        this.headers.put("Content-Type", contentType.toString());
    }

    // 본문에 대한 writer를 가져오는 메소드
    public PrintWriter getWriter() {
        // getWriter 중복 호출 방지용
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(bodyBuffer, UTF_8), false);
        }
        return writer;
    }

    // 본문 출력 스트림 바로 가져오는 메소드
    public OutputStream getOutputStream() {
        return bodyBuffer;
    }

    public void flush() throws IOException {
        // flush 중복 호출 방지용
        if(committed) return;
        committed = true;

        byte[] bodyBytes = bodyBuffer.toByteArray();

        PrintWriter headerWriter = new PrintWriter(new OutputStreamWriter(outputStream, UTF_8), false);

        headerWriter.println("HTTP/1.1 " + httpStatus);
        for(Map.Entry<String, String> header : headers.entrySet()) {
            headerWriter.println(header.getKey() + ": " + header.getValue());
        }
        headerWriter.println("Content-Length: " + bodyBytes.length);
        headerWriter.println();
        headerWriter.flush();

        outputStream.write(bodyBytes);
        outputStream.flush();
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "httpStatus=" + httpStatus +
                ", headers=" + headers +
                '}';
    }
}
