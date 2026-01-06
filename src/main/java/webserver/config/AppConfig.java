package webserver.config;

import webserver.core.Dispatcher;
import webserver.dispatch.*;
import webserver.filter.AccessLogFilter;
import webserver.filter.RequestFilter;
import webserver.handler.*;
import webserver.interceptor.AuthInterceptor;
import webserver.interceptor.HandlerInterceptor;
import webserver.router.Router;
import webserver.staticresource.StaticResourceHandler;

import java.util.List;

public class AppConfig {

    private final Router router = createRouter();
    private final StaticResourceHandler staticResourceHandler = new StaticResourceHandler();

    public Dispatcher dispatcher() {
        return new Dispatcher(
                handlerMappings(),
                handlerAdapters()
        );
    }

    public List<HandlerMapping> handlerMappings() {
        return List.of(
                new RouterHandlerMapping(router, interceptors()), // 인터셉터 추가필요
                new StaticResourceHandlerMapping(staticResourceHandler)
        );
    }

    public List<HandlerAdapter> handlerAdapters() {
        return List.of(
                new SimpleHandlerAdapter(),
                new StaticResourceHandlerAdapter()
        );
    }

    public List<RequestFilter> requestFilters() {
        return List.of(
                new AccessLogFilter()
        );
    }

    public List<HandlerInterceptor> interceptors() {
        return List.of(
                new AuthInterceptor()
        );
    }

    private Router createRouter() {
        Router router = new Router();
        router.register("POST", "/create", new CreateUserHandler());
        return router;
    }

}
