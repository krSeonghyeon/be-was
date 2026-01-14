package webserver.handler;

import model.user.User;
import webserver.argument.annotation.CurrentUser;
import webserver.view.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class CommentHandler implements Handler {

    public ModelAndView handle(@CurrentUser User user) {
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getName());
        return new ModelAndView("comment", model);
    }
}
