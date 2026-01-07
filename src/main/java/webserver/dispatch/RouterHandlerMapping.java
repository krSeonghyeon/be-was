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
        Object handler = router.route(request.getMethod(), request.getPath()); // 추후 핸들러마다 인터셉터 다르게 매핑
        if (handler == null) return null;
        return new HandlerExecutionChain(handler, interceptors);
    }
}
