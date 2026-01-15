package webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    private HttpRequestParser() {}

    public static HttpRequest parse(InputStream in) throws IOException {

        String requestLine = readLine(in);
        String[] tokens = requestLine.split(" ");
        String method = tokens[0];
        String url = tokens[1];
        String httpVersion = tokens[2];

        String path = url;
        String query = "";

        if (url.contains("?")) {
            String[] split = url.split("\\?", 2);
            path = split[0];
            query = split[1];
        }

        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = readLine(in)).isEmpty()) {
            int idx = line.indexOf(":");
            if (idx > 0) {
                String key = line.substring(0, idx).trim().toLowerCase();
                String value = line.substring(idx + 1).trim();
                headers.put(key, value);
            }
        }

        byte[] body = new byte[0];
        if (headers.containsKey("content-length")) {
            int length = Integer.parseInt(headers.get("content-length"));
            body = in.readNBytes(length);
        }

        return new HttpRequest(method, path, query, httpVersion, headers, body);
    }

    private static String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        boolean r = false;

        while ((c = in.read()) != -1) {
            if (c == '\r') {
                r = true;
            } else if (c == '\n' && r) {
                break;
            } else {
                r = false;
                sb.append((char)c);
            }
        }
        return sb.toString();
    }
}
