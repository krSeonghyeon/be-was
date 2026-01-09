package webserver.handler;

import model.user.User;
import webserver.argument.annotation.CurrentUser;
import webserver.view.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class IndexHandler implements Handler {

    public ModelAndView handle(@CurrentUser User user) {
        Map<String, Object> model = new HashMap<>();

        if (user != null) {
            model.put("isLogin", true);
            model.put("username", user.getName());
        } else {
            model.put("isLogin", false);
        }

        return new ModelAndView("index.html", model);
    }
}
