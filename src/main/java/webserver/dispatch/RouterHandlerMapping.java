package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.router.Router;

public class RouterHandlerMapping implements HandlerMapping {

    private final Router router;

    public RouterHandlerMapping(Router router) {
        this.router = router;
    }

    @Override
    public Object getHandler(HttpRequest request) {
        return router.route(request.method(), request.path());
    }
}
