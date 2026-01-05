package webserver.core;

import java.io.*;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.filter.RequestFilter;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;
import webserver.http.QueryStringParser;
import webserver.router.Handler;
import webserver.router.Router;
import webserver.staticresource.StaticResourceHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Router router;
    private final StaticResourceHandler staticResourceHandler;
    private final List<RequestFilter> filters;

    public RequestHandler(Socket connectionSocket,
                          Router router,
                          StaticResourceHandler staticResourceHandler,
                          List<RequestFilter> filters
    ) {
        this.connection = connectionSocket;
        this.router = router;
        this.staticResourceHandler = staticResourceHandler;
        this.filters = filters;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
        ) {
            HttpRequest request = HttpRequestParser.parser(in);
            if (request == null) {
                return;
            }

            for (RequestFilter filter : filters) {
                filter.doFilter(request);
            }

            Handler handler = router.route(request.method(), request.path());

            // http 입력 로깅 다시 부활시키기 (HttpRequest 활용)

            HttpResponse response;
            if (handler != null) {
                response = handler.handle(request);
            } else {
                response = staticResourceHandler.handle(request.path());
            }
            response.writeTo(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
