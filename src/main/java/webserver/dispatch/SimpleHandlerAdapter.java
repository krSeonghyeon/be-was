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
    public HttpResponse handle(HttpRequest request, Object handler) {
        return ((Handler) handler).handle(request);
    }
}
