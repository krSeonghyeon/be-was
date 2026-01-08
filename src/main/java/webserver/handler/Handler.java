package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

@FunctionalInterface
public interface Handler {

    ModelAndView handle(HttpRequest request, HttpResponse response);
}
