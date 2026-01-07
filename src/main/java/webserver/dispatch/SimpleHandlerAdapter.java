package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.router.Handler;

public class SimpleHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Handler;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, Object handler) {
        ((Handler) handler).handle(request, response);
    }
}
