package webserver.exception;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.view.ModelAndView;

import java.util.Map;

public class BasicExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpRequest request, HttpResponse response, Exception ex) {

        if (ex instanceof HttpException httpEx) {
            response.setStatus(httpEx.getStatus());

            if (httpEx instanceof MethodNotAllowedException mEx) {
                response.setHeader("Allow", String.join(", ", mEx.getAllowedMethods()));
            }

            return new ModelAndView("error.html", Map.of(
                    "status", httpEx.getStatus().getCode(),
                    "message", httpEx.getMessage()
            ));
        }

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ModelAndView("error.html", Map.of(
                "status", HttpStatus.INTERNAL_SERVER_ERROR.getCode(),
                "message", "Internal Server Error"
        ));
    }
}
