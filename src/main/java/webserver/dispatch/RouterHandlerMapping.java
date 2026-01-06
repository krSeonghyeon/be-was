package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.interceptor.HandlerInterceptor;
import webserver.router.Router;

import java.util.List;

public class RouterHandlerMapping implements HandlerMapping {

    private final Router router;
    private final List<HandlerInterceptor> interceptors;

    public RouterHandlerMapping(Router router, List<HandlerInterceptor> interceptors) {
        this.router = router;
        this.interceptors = interceptors;
    }

    @Override
    public HandlerExecutionChain getHandler(HttpRequest request) {
        Object handler = router.route(request.method(), request.path());
        if (handler == null) return null;
        return new HandlerExecutionChain(handler, interceptors);
    }
}
