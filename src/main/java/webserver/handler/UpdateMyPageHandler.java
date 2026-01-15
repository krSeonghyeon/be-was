package webserver.handler;

import db.UserDatabase;
import model.user.User;
import webserver.argument.annotation.CurrentUser;
import webserver.exception.BadRequestException;
import webserver.http.HttpRequest;
import webserver.http.QueryStringParser;
import webserver.view.ModelAndView;

import java.util.Map;

public class UpdateMyPageHandler implements Handler {

    private final UserDatabase userDatabase;

    public UpdateMyPageHandler(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public ModelAndView handle(@CurrentUser User user, HttpRequest request) {
        String body = new String(request.getBody());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String name = getFirst(params, "username");
        String password = getFirst(params, "password");
        String passwordConfirm = getFirst(params, "passwordConfirm");

        if (name == null || name.isBlank()) {
            throw new BadRequestException("username required");
        }

        user.setName(name);

        if (password != null && !password.isBlank()) {
            if (!password.equals(passwordConfirm)) {
                throw new BadRequestException("password confirm mismatch");
            }
            user.setPassword(password);
        }

        userDatabase.update(user);
        return new ModelAndView("redirect:/");
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
