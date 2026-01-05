package webserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;

import java.util.Map;

public class AccessLogFilter implements RequestFilter {

    private static final Logger log = LoggerFactory.getLogger("ACCESS_LOG");

    @Override
    public void doFilter(HttpRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append(request.method())
                .append(" ")
                .append(request.query().isEmpty() ? request.path() : request.path() + "?" + request.query())
                .append(" ")
                .append(request.httpVersion())
                .append("\n");

        for (Map.Entry<String, String> header : request.headers().entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\n");
        }

        log.debug(sb.toString());
    }
}
