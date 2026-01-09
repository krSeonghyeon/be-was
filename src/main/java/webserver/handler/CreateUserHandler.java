package webserver.handler;

import db.Database;
import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.QueryStringParser;
import webserver.view.ModelAndView;

import java.util.Map;

public class CreateUserHandler implements Handler {

    public ModelAndView handle(HttpRequest request, HttpResponse response) {
        String body = new String(request.getBody());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String userId = getFirst(params, "userId");
        String password = getFirst(params, "password");
        String name = getFirst(params, "name");

        if (userId == null || password == null || name == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            return null;
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
