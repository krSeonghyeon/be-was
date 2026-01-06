package webserver.core;

import java.io.*;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.filter.DefaultFilterChain;
import webserver.filter.FilterChain;
import webserver.filter.RequestFilter;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final List<RequestFilter> filters;
    private final Dispatcher dispatcher;

    public RequestHandler(
            Socket connectionSocket,
            List<RequestFilter> filters,
            Dispatcher dispatcher
    ) {
        this.connection = connectionSocket;
        this.filters = filters;
        this.dispatcher = dispatcher;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             DataOutputStream dos = new DataOutputStream(out);
        ) {
            HttpRequest request = HttpRequestParser.parser(br);
            if (request == null) {
                return;
            }

            FilterChain chain = new DefaultFilterChain(
                    filters,
                    () -> {
                        try {
                            dispatcher.dispatch(request, dos);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            chain.doFilter(request);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
