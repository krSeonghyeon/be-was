package webserver.core;

import java.io.*;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.filter.RequestFilter;
import webserver.dispatch.HandlerAdapter;
import webserver.dispatch.HandlerMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final List<RequestFilter> filters;

    public RequestHandler(Socket connectionSocket,
                          List<HandlerMapping> handlerMappings,
                          List<HandlerAdapter> handlerAdapters,
                          List<RequestFilter> filters
    ) {
        this.connection = connectionSocket;
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
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

            Object handler = getHandler(request);

            HttpResponse response;
            if (handler == null) {
                response = HttpResponse.notFound();
            } else {
                HandlerAdapter adapter = getHandlerAdapter(handler);
                response = adapter.handle(request, handler);
            }
            response.writeTo(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalStateException("No adapter for handler: " + handler);
    }

    private Object getHandler(HttpRequest request) {
        Object handler = null;
        for (HandlerMapping mapping : handlerMappings) {
            handler = mapping.getHandler(request);
            if (handler != null) break;
        }
        return handler;
    }
}
