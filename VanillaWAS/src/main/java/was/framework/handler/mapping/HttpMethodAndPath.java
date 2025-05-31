package main.java.was.framework.handler.mapping;

import main.java.was.http.header.HttpMethod;

import java.util.Objects;

public class HttpMethodAndPath {
    private HttpMethod httpMethod;
    private String path;

    public HttpMethodAndPath() {

    }

    public HttpMethodAndPath(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpMethodAndPath that = (HttpMethodAndPath) o;
        return httpMethod == that.httpMethod && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, path);
    }
}
