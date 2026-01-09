package webserver.exception;

import webserver.http.HttpStatus;

import java.util.Set;

public class MethodNotAllowedException extends HttpException {

    private final Set<String> allowedMethods;

    public MethodNotAllowedException(String message, Set<String> allowedMethods) {
        super(HttpStatus.METHOD_NOT_ALLOWED, message);
        this.allowedMethods = allowedMethods;
    }

    public Set<String> getAllowedMethods() {
        return allowedMethods;
    }
}
