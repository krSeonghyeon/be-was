package webserver.handler;

import webserver.http.HttpRequest;

public class StaticResourceHandlerMapping implements HandlerMapping {

    private final StaticResourceHandler staticResourceHandler;

    public StaticResourceHandlerMapping(StaticResourceHandler staticResourceHandler) {
        this.staticResourceHandler = staticResourceHandler;
    }

    @Override
    public Object getHandler(HttpRequest request) {
        return staticResourceHandler.exists(request.path()) ? staticResourceHandler : null;
    }
}
