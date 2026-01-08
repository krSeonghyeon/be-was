package webserver.handler;

import db.Database;
import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.QueryStringParser;
import db.SessionManager;
import webserver.view.ModelAndView;

import java.util.Map;

public class LoginHandler implements Handler {

    @Override
    public ModelAndView handle(HttpRequest request, HttpResponse response) {
        String body = new String(request.getBody());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");

        if (userId == null || password == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            return null;
        }

        User user = Database.findUserById(userId);
        if (!authenticate(user, password)) {
            return new ModelAndView("redirect:/login?error=true");
        }

        String sessionId = SessionManager.createSession(user);
        response.setHeader(
                "Set-Cookie",
                "SID=" + sessionId + "; Path=/"
        );

        return new ModelAndView("redirect:/");
    }

    private boolean authenticate(User user, String password) {
        return user != null && user.getPassword().equals(password);
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
