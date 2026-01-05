package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    HttpResponse handle(HttpRequest request, Object handler) throws IOException;
}
