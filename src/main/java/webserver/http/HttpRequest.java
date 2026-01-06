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
}
