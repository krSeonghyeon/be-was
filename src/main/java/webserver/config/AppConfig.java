package webserver.config;

import webserver.filter.AccessLogFilter;
import webserver.filter.RequestFilter;
import webserver.handler.CreateUserHandler;
import webserver.router.Router;
import webserver.staticresource.StaticResourceHandler;

import java.util.List;

public class AppConfig {

    private final Router router = createRouter();
    private final StaticResourceHandler staticResourceHandler = new StaticResourceHandler();
    private final List<RequestFilter> requestFilters = List.of(new AccessLogFilter());

    public Router router() {
        return router;
    }

    public StaticResourceHandler staticResourceHandler() {
        return staticResourceHandler;
    }

    public List<RequestFilter> requestFilters() {
        return requestFilters;
    }

    private Router createRouter() {
        Router router = new Router();
        router.register("GET", "/create", new CreateUserHandler());
        router.register("POST", "/create", new CreateUserHandler());
        return router;
    }

}
