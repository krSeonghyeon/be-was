package webserver.exception;

import webserver.http.HttpStatus;

public class NotFoundException extends HttpException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
