package webserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;

import java.util.Map;

public class AccessLogFilter implements RequestFilter {

    private static final Logger log = LoggerFactory.getLogger("ACCESS_LOG");

    @Override
    public void doFilter(HttpRequest request, FilterChain chain) throws Exception {
        long start = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod())
                .append(" ")
                .append(request.getQuery().isEmpty() ? request.getPath() : request.getPath() + "?" + request.getQuery())
                .append(" ")
                .append(request.getHttpVersion())
                .append("\n");

        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\n");
        }

        try {
            chain.doFilter(request);
        } finally {
            long gap = System.currentTimeMillis() - start;
            log.debug(sb.append("Elapsed: ").append(gap).append("ms\n").toString());
        }
    }
}
