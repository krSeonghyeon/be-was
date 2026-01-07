package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.router.Handler;
import webserver.session.SessionManager;

public class LogoutHandler implements Handler {

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String sessionId = request.getCookie("SID");

        if (sessionId != null) {
            SessionManager.invalidate(sessionId);
        }

        response.setStatus(HttpStatus.FOUND);
        response.setHeader("Location", "/login");
        response.setHeader(
                "Set-Cookie",
                "SID=; Path=/; Max-Age=0"
        );
    }
}
