package webserver.dispatch;

import webserver.staticresource.StaticResourceHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public class StaticResourceHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof StaticResourceHandler;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, Object handler) throws IOException {
        ((StaticResourceHandler) handler).handle(request.getPath(), response);
    }
}
