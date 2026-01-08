package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private String query;
    private String httpVersion;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    private Map<String, Object> attributes = new HashMap<>(); // TODO: ArgumentResolver로 바꾸기

    public HttpRequest(
            String method,
            String path,
            String query,
            String httpVersion,
            Map<String, String> headers,
            byte[] body
    ) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    public String getCookie(String name) {
        String cookieHeader = getHeader("cookie");
        if (cookieHeader == null) return null;

        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            String[] kv = cookie.trim().split("=", 2);
            if (kv.length == 2 && kv[0].equals(name)) {
                return kv[1];
            }
        }
        return null;
    }

    public String getMethod() {
        return method;
    }

    public String getQuery() {
        return query;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getPath() {
        return path;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
}
