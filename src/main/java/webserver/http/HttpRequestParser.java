package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.util.QueryStringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HttpRequestParser {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    private HttpRequestParser() {}

    public static HttpRequest parser(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }

        StringBuilder accessLog = new StringBuilder();
        accessLog.append(requestLine).append("\n");

        String[] tokens = requestLine.split(" ");
        String method = tokens[0];
        String url = tokens[1];

        String path = url;
        String query = "";

        if (url.contains("?")) {
            String[] split = url.split("\\?", 2);
            path = split[0];
            query = split[1];
        }

        Map<String, String[]> params = QueryStringParser.parse(query);

        while (true) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            accessLog.append(line).append("\n");
        }

        logger.debug("[ACCESS]\n{}", accessLog);

        return new HttpRequest(method, path, params);
    }
}
