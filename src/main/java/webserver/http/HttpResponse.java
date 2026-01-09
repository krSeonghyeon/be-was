package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private Map<String, String> headers = new LinkedHashMap<>();
    private byte[] body;

    public HttpResponse() {

    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
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
}
