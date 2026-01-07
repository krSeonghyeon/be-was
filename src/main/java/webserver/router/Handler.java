package webserver.router;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

@FunctionalInterface
public interface Handler {

    void handle(HttpRequest request, HttpResponse response);
}
