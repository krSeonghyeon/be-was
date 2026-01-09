package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import db.SessionManager;
import webserver.view.ModelAndView;

public class LogoutHandler implements Handler {

    public ModelAndView handle(HttpRequest request, HttpResponse response) {
        String sessionId = request.getCookie("SID");

        if (sessionId != null) {
            SessionManager.invalidate(sessionId);
        }

        response.setHeader(
                "Set-Cookie",
                "SID=; Path=/; Max-Age=0"
        );
        return new ModelAndView("redirect:/login");
    }
}
