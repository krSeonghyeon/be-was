package webserver.core;

import webserver.dispatch.HandlerAdapter;
import webserver.dispatch.HandlerExecutionChain;
import webserver.dispatch.HandlerMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.util.List;

public class Dispatcher {

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public Dispatcher(List<HandlerMapping> handlerMappings, List<HandlerAdapter> handlerAdapters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    public void dispatch(HttpRequest request, HttpResponse response) throws Exception {
        HandlerExecutionChain chain = getHandlerExecutionChain(request);

        if (chain == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            return;
        }

        Exception dispatchException = null;

        try {
            if (!chain.applyPreHandle(request, response)) {
                return;
            }
            Object handler = chain.getHandler();
            HandlerAdapter adapter = getHandlerAdapter(handler);
            adapter.handle(request, response, handler);
            chain.applyPostHandle(request, response);
        } catch (Exception e) {
            dispatchException = e;
            throw e;
        } finally {
            chain.triggerAfterCompletion(request, dispatchException);
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
