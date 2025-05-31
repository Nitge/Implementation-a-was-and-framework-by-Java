package main.java.was.http;

import main.java.was.http.header.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static main.java.util.Logger.log;

public class HttpRequest {
    private HttpMethod method; // HTTP 메소드
    private String path; // 요청 경로 URL
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
        parseRequestLine(reader);
        parseHeaders(reader);
        parseBody(reader);
    }

    private void parseRequestLine(BufferedReader reader) throws IOException {

        // http 메시지의 첫 줄(start line)을 읽음
        String startLine = reader.readLine();

        // http 형식에 맞지 않을 경우 예외 처리 (내용이 없을 경우)
        if (startLine == null) {
            throw new IOException("EOF: No start line received");
        }

        // http 메시지의 start line은 각 요소가 공백으로 구분되어 있으므로 parsing
        String[] parts = startLine.split(" ");

        // 예시
        // path[0] = GET
        // path[1] = /site?q=hello&page=3
        // path[2] = HTTP/1.1

        // http 형식에 맞지 않을 경우 예외 처리 (요소가 3가지가 아닐 경우)
        if(parts.length != 3) {
            throw new IOException("Invalid start line: " + startLine);
        }

        method = HttpMethod.valueOf(parts[0]); // http 메소드 저장

        // "?" 기준으로 경로와 쿼리 파라미터를 분리
        String[] pathParts = parts[1].split("\\?");
        path = pathParts[0]; // 요청 경로 저장

        // 쿼리파라미터가 존재할 경우 parsing 처리
        if(pathParts.length > 1) {
            parseQueryParameters(pathParts[1]);
        }
    }

    private void parseQueryParameters(String queryString) {
        String[] split = queryString.split("&");
        // ex)
        // split[0] = "q=hello"
        // split[1] = "page=3"

        for (String param : split) {
            String[] keyValue = param.split("=");
            // ex)
            // keyValue[0] = q
            // keyValue[1] = hello

            // Percent-encoding으로 넘어오는 한글이 있을 수 있으므로 디코딩 처리
            String key = URLDecoder.decode(keyValue[0], UTF_8);

            // http 요청 시 key는 존재하지만 value가 같이 넘어오지 않는 경우도 있다
            String value = keyValue.length > 1 ?
                    URLDecoder.decode(keyValue[1], UTF_8) : "";
            queryParameters.put(key, value);
        }
    }

    private void parseHeaders(BufferedReader reader) throws IOException {
        String line;
        while(!(line = reader.readLine()).isEmpty()) {
            String[] headerParts = line.split(":");
            // trim 앞뒤 공백 제거
            headers.put(headerParts[0].trim(), headerParts[1].trim());
        }
    }

    private void parseBody(BufferedReader reader) throws IOException {
        // Content-Length 헤더가 없다 = 바디가 없음
        if(!headers.containsKey("Content-Length")) {
            return;
        }
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        char[] bodyChars = new char[contentLength];
        int read = reader.read(bodyChars);
        if(read != contentLength) {
            throw new IOException("Invalid request body");
        }
        String body = new String(bodyChars);

        String contentType = headers.get("Content-Type");
        if("application/x-www-form-urlencoded".equals(contentType)) {
            parseQueryParameters(body);
        }

    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", queryParameters=" + queryParameters +
                '}';
    }
}
