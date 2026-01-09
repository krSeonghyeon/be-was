package webserver.exception;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

public interface HandlerExceptionResolver {

    ModelAndView resolveException(HttpRequest request, HttpResponse response, Exception ex);
}
