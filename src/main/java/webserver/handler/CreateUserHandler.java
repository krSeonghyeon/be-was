package webserver.handler;

import db.Database;
import model.user.User;
import webserver.exception.BadRequestException;
import webserver.http.HttpRequest;
import webserver.http.QueryStringParser;
import webserver.view.ModelAndView;

import java.util.Map;

public class CreateUserHandler implements Handler {

    public ModelAndView handle(HttpRequest request) {
        String body = new String(request.getBody());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");
        String name = getFirst(params, "name");

        if (userId == null || password == null || name == null) {
            throw new BadRequestException("Missing parameter");
        }

        User user = new User(userId, password, name);
        Database.addUser(user);

        return new ModelAndView("redirect:/");
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
