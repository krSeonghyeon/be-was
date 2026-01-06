package webserver.core;

import webserver.dispatch.HandlerAdapter;
import webserver.dispatch.HandlerExecutionChain;
import webserver.dispatch.HandlerMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.DataOutputStream;
import java.util.List;

public class Dispatcher {

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public Dispatcher(List<HandlerMapping> handlerMappings, List<HandlerAdapter> handlerAdapters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    public void dispatch(HttpRequest request, DataOutputStream dos) throws Exception {
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
