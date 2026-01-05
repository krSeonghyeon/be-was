package webserver.handler;

import webserver.http.HttpRequest;

@FunctionalInterface
public interface HandlerMapping {

    Object getHandler(HttpRequest request);
}
