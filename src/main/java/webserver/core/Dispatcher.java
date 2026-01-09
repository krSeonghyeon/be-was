package webserver.core;

import webserver.dispatch.HandlerAdapter;
import webserver.dispatch.HandlerExecutionChain;
import webserver.dispatch.HandlerMapping;
import webserver.exception.HandlerExceptionResolver;
import webserver.exception.NotFoundException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;
import webserver.view.View;
import webserver.view.ViewResolver;

import java.util.List;

public class Dispatcher {

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final List<HandlerExceptionResolver> exceptionResolvers;
    private final ViewResolver viewResolver;

    public Dispatcher(
            List<HandlerMapping> handlerMappings,
            List<HandlerAdapter> handlerAdapters,
            List<HandlerExceptionResolver> exceptionResolvers,
            ViewResolver viewResolver
    ) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
        this.exceptionResolvers = exceptionResolvers;
        this.viewResolver = viewResolver;
    }

    public void dispatch(HttpRequest request, HttpResponse response) throws Exception {
        Exception dispatchException = null;
        ModelAndView mv = null;
        HandlerExecutionChain chain = null;

        try {
            chain = getHandlerExecutionChain(request);
            if (chain == null) {
                throw new NotFoundException("Chain Not Found");
            }
            if (!chain.applyPreHandle(request, response)) {
                return;
            }
            Object handler = chain.getHandler();
            HandlerAdapter adapter = getHandlerAdapter(handler);
            mv = adapter.handle(request, response, handler);
            chain.applyPostHandle(request, response);
        } catch (Exception e) {
            dispatchException = e;
            mv = processHandlerException(request, response, e);
            if (mv == null) {
                throw e;
            }
        } finally {
            if (chain != null) {
                chain.triggerAfterCompletion(request, dispatchException);
            }
        }

        render(mv, request, response);
    }

    private ModelAndView processHandlerException(HttpRequest request, HttpResponse response, Exception ex) {
        for (HandlerExceptionResolver resolver : exceptionResolvers) {
            ModelAndView mv = resolver.resolveException(request, response, ex);
            if (mv != null) {
                return mv;
            }
        }
        return null;
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
