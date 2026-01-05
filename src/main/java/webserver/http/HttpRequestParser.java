package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    private HttpRequestParser() {}

    public static HttpRequest parser(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }

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
        while((line = br.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split(":", 2);
            headers.put(parts[0].trim().toLowerCase(), parts[1].trim());
        }

        byte[] body = null;
        if (headers.containsKey("content-length")) {
            int len = Integer.parseInt(headers.get("content-length"));
            char[] buf = new char[len];
            int read = br.read(buf, 0, len);
            body = read > 0 ?
                    new String(buf, 0, read).getBytes(StandardCharsets.UTF_8) : new byte[0];
        }

        return new HttpRequest(method, path, query, httpVersion, headers, body);
    }
}
