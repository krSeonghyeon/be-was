package webserver.exception;

import webserver.http.HttpStatus;

public class HttpException extends RuntimeException {

    private final HttpStatus status;

    protected HttpException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
