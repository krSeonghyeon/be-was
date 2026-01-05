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
        Map<String, String[]> params =
                QueryStringParser.parse(request.query());

        if (params.isEmpty()) {
            return HttpResponse.badRequest();
        }

        User user = createUser(params);
        Database.addUser(user);
        return HttpResponse.redirect("/");
    }

    private User createUser(Map<String, String[]> params) {
        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");
        String name = getFirst(params, "name");
        return new User(userId, password, name);
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
