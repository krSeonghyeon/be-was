package webserver.handler;

import db.Database;
import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.QueryStringParser;
import webserver.router.Handler;
import webserver.session.SessionManager;

import java.util.Map;

public class LoginHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        String body = new String(request.body());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");

        if (userId == null || password == null) {
            return HttpResponse.badRequest();
        }

        User user = Database.findUserById(userId);
        if (!authenticate(user, password)) {
            return HttpResponse.redirect("/login.html?error=true"); // TODO: 수정필요(에러메시지)
        }

        String sessionId = SessionManager.createSession(user);
        HttpResponse response = HttpResponse.redirect("/index.html");
        response.addHeader(
                "Set-Cookie",
                "SID=" + sessionId + "; Path=/"
        );
        return response;
    }

    private boolean authenticate(User user, String password) {
        return user != null && user.getPassword().equals(password);
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
