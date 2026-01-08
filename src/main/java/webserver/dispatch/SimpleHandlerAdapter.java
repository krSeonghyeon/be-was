package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.handler.Handler;
import webserver.view.ModelAndView;

public class SimpleHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Handler;
    }

    @Override
    public ModelAndView handle(HttpRequest request, HttpResponse response, Object handler) {
        return ((Handler) handler).handle(request, response);
    }
}
