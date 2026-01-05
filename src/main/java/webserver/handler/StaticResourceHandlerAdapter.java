package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public class StaticResourceHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof StaticResourceHandler;
    }

    @Override
    public HttpResponse handle(HttpRequest request, Object handler) throws IOException {
        return ((StaticResourceHandler) handler).handle(request.path());
    }
}
