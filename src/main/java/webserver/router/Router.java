package webserver.router;

import webserver.handler.Handler;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<RouteKey, Handler> routes = new HashMap<>();

    public void register(String method, String path, Handler handler) {
        routes.put(new RouteKey(method, path), handler);
    }

    public Handler route(String method, String path) {
        return routes.get(new RouteKey(method, path));
    }
}
