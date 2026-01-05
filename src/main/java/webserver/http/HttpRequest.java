package webserver.http;

import java.util.Map;

public record HttpRequest(
        String method,
        String path,
        String query,
        String httpVersion,
        Map<String, String> headers,
        byte[] body
) {

}
