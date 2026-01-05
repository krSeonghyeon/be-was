package webserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;

public class AccessLogFilter implements RequestFilter {

    private static final Logger log = LoggerFactory.getLogger("ACCESS_LOG");

    @Override
    public void doFilter(HttpRequest request) {
        String url = request.query().isEmpty() ? request.path() : request.path() + "?" + request.query();

        log.debug("{} {} {}\nheaders={}",
                request.method(),
                url,
                request.httpVersion(),
                request.headers()
        );
    }
}
