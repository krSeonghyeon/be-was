package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {

    private final HttpStatus status;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private byte[] body;

    private HttpResponse(HttpStatus status) {
        this.status = status;
    }

    public static HttpResponse ok(byte[] body, String contentType) {
        HttpResponse response = new HttpResponse(HttpStatus.OK);
        response.body = body;
        response.headers.put("Content-Type", contentType);
        response.headers.put("Content-Length", String.valueOf(body.length));
        return response;
    }

    public static HttpResponse redirect(String location) {
        HttpResponse response = new HttpResponse(HttpStatus.FOUND);
        response.headers.put("Location", location);
        response.headers.put("Content-Length", "0");
        return response;
    }

    public static HttpResponse badRequest() {
        HttpResponse response = new HttpResponse(HttpStatus.BAD_REQUEST);
        response.headers.put("Content-Length", "0");
        return response;
    }

    public static HttpResponse forbidden() {
        HttpResponse response = new HttpResponse(HttpStatus.FORBIDDEN);
        response.headers.put("Content-Length", "0");
        return response;
    }

    public static HttpResponse notFound() {
        HttpResponse response = new HttpResponse(HttpStatus.NOT_FOUND);
        response.headers.put("Content-Length", "0");
        return response;
    }

    public void writeTo(DataOutputStream dos) throws IOException {
        dos.writeBytes(
                "HTTP/1.1 " +
                   status.getCode() + " " +
                   status.getReason() + "\r\n"
        );

        for (Map.Entry<String, String> header : headers.entrySet()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }

        dos.writeBytes("\r\n");

        if (body != null) {
            dos.write(body);
        }
        dos.flush();
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }
}
