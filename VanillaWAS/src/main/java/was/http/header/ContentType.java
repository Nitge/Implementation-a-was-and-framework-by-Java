package main.java.was.http.header;

public enum ContentType {
    HTML("text/html; charset=UTF-8"),
    JSON("application/json; charset=UTF-8"),
    PLAIN("text/plain; charset=UTF-8"),
    XML("application/xml; charset=UTF-8"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    PDF("application/pdf"),
    ICO("image/x-icon"),;

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
