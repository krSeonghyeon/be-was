package webserver.dispatch;

import webserver.http.HttpRequest;

@FunctionalInterface
public interface HandlerMapping {

    HandlerExecutionChain getHandler(HttpRequest request);
}
