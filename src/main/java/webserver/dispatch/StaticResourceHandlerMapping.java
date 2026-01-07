package webserver.dispatch;

import webserver.staticresource.StaticResourceHandler;
import webserver.http.HttpRequest;

import java.util.List;

public class StaticResourceHandlerMapping implements HandlerMapping {

    private final StaticResourceHandler staticResourceHandler;

    public StaticResourceHandlerMapping(StaticResourceHandler staticResourceHandler) {
        this.staticResourceHandler = staticResourceHandler;
    }

    @Override
    public HandlerExecutionChain getHandler(HttpRequest request) {
        if (!staticResourceHandler.exists(request.getPath())) {
            return null;
        }

        return new HandlerExecutionChain(
                staticResourceHandler,
                List.of()
        );
    }
}
