package webserver.handler;

import db.Database;
import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.QueryStringParser;
import webserver.router.Handler;

import java.util.Map;

public class CreateUserHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        if (request.body() == null || request.body().length == 0) {
            return HttpResponse.badRequest();
        }

        String body = new String(request.body());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");
        String name = getFirst(params, "name");

        if (userId == null || password == null || name == null) {
            return HttpResponse.badRequest();
        }

        User user = new User(userId, password, name);
        Database.addUser(user);
        return HttpResponse.redirect("/index.html");
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
