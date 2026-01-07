package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    void handle(HttpRequest request, HttpResponse response, Object handler) throws IOException;
}
