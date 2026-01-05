package webserver.core;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public RequestHandler(Socket connectionSocket,
                          Router router,
                          StaticResourceHandler staticResourceHandler
    ) {
        this.connection = connectionSocket;
        this.router = router;
        this.staticResourceHandler = staticResourceHandler;
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
