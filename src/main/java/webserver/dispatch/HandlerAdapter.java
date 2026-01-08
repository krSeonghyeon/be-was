package webserver.dispatch;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(HttpRequest request, HttpResponse response, Object handler) throws IOException;
}
