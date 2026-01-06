package webserver.core;

import java.io.*;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.dispatch.HandlerExecutionChain;
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
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             DataOutputStream dos = new DataOutputStream(out);
        ) {
            HttpRequest request = HttpRequestParser.parser(br);
            if (request == null) {
                return;
            }

            for (RequestFilter filter : filters) {
                filter.doFilter(request); // TODO: 전/후 필터체인 형태로 바꾸기
            }

            HandlerExecutionChain chain = getHandlerExecutionChain(request);

            HttpResponse response;
            if (chain == null) {
                response = HttpResponse.notFound();
            } else {
                Exception dispatchException = null;
                try {
                    if (!chain.applyPreHandle(request)) {
                        response = HttpResponse.forbidden();
                    } else {
                        Object handler = chain.getHandler();
                        HandlerAdapter adapter = getHandlerAdapter(handler);
                        response = adapter.handle(request, handler);
                        chain.applyPostHandle(request, response);
                    }
                } catch (Exception e) {
                    dispatchException = e;
                    throw e;
                } finally {
                    chain.triggerAfterCompletion(request, dispatchException);
                }
            }
            response.writeTo(dos);
        } catch (Exception e) {
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

    private HandlerExecutionChain getHandlerExecutionChain(HttpRequest request) {
        for (HandlerMapping mapping : handlerMappings) {
            HandlerExecutionChain chain = mapping.getHandler(request);
            if (chain != null) return chain;
        }
        return null;
    }
}
