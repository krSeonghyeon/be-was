package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.router.Handler;
import webserver.session.SessionManager;

public class LogoutHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        String sessionId = request.getCookie("SID");

        if (sessionId != null) {
            SessionManager.invalidate(sessionId);
        }

        HttpResponse response = HttpResponse.redirect("/login.html");
        response.addHeader(
                "Set-Cookie",
                "SID=; Path=/; Max-Age=0"
        );

        return response;
    }
}
