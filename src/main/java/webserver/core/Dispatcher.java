package webserver.core;

import webserver.dispatch.HandlerAdapter;
import webserver.dispatch.HandlerExecutionChain;
import webserver.dispatch.HandlerMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.view.ModelAndView;
import webserver.view.View;
import webserver.view.ViewResolver;

import java.util.List;

public class Dispatcher {

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final ViewResolver viewResolver;

    public Dispatcher(
            List<HandlerMapping> handlerMappings,
            List<HandlerAdapter> handlerAdapters,
            ViewResolver viewResolver
    ) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
        this.viewResolver = viewResolver;
    }

    public void dispatch(HttpRequest request, HttpResponse response) throws Exception {
        HandlerExecutionChain chain = getHandlerExecutionChain(request);

        if (chain == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            return;
        }

        Exception dispatchException = null;
        ModelAndView mv = null;

        try {
            if (!chain.applyPreHandle(request, response)) {
                return;
            }
            Object handler = chain.getHandler();
            HandlerAdapter adapter = getHandlerAdapter(handler);

            mv = adapter.handle(request, response, handler);
            chain.applyPostHandle(request, response);
        } catch (Exception e) {
            dispatchException = e;
            throw e;
        } finally {
            chain.triggerAfterCompletion(request, dispatchException);
        }

        render(mv, request, response);
    }

    private void render(ModelAndView mv, HttpRequest request, HttpResponse response) {
        if (mv == null) return; // response 출력한 경우 뷰렌더링 스킵
        View view = viewResolver.resolveViewName(mv.getViewName());
        view.render(mv.getModel(), request, response);
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
