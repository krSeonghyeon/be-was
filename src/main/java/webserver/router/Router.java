package webserver.router;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<String, Handler> routes = new HashMap<>();

    public void register(String path, Handler handler) {
        routes.put(path, handler);
    }

    public Handler route(String path) {
        return routes.get(path);
    }
}
