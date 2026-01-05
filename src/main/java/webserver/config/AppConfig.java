package webserver.config;

import webserver.dispatch.*;
import webserver.filter.AccessLogFilter;
import webserver.filter.RequestFilter;
import webserver.handler.*;
import webserver.router.Router;
import webserver.staticresource.StaticResourceHandler;

import java.util.List;

public class AppConfig {

    private final Router router = createRouter();
    private final StaticResourceHandler staticResourceHandler = new StaticResourceHandler();

    public List<HandlerMapping> handlerMappings() {
        return List.of(
                new RouterHandlerMapping(router),
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

    private Router createRouter() {
        Router router = new Router();
        router.register("GET", "/create", new CreateUserHandler());
        router.register("POST", "/create", new CreateUserHandler());
        return router;
    }

}
