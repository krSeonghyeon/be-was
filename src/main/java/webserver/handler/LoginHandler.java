package webserver.handler;

import db.Database;
import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.QueryStringParser;
import webserver.router.Handler;
import webserver.session.SessionManager;

import java.util.Map;

public class LoginHandler implements Handler {

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String body = new String(request.getBody());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");

        if (userId == null || password == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            return;
        }

        User user = Database.findUserById(userId);
        if (!authenticate(user, password)) {
            response.setStatus(HttpStatus.FOUND);
            response.setHeader("Location", "/login?error=true");
            return;
        }

        String sessionId = SessionManager.createSession(user);
        response.setStatus(HttpStatus.FOUND);
        response.setHeader("Location", "/");
        response.setHeader(
                "Set-Cookie",
                "SID=" + sessionId + "; Path=/"
        );
    }

    private boolean authenticate(User user, String password) {
        return user != null && user.getPassword().equals(password);
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
