package webserver.handler;

import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class IndexHandler implements Handler {

    @Override
    public ModelAndView handle(HttpRequest request, HttpResponse response) {
        User user = (User)request.getAttribute("USER"); // 나중에 argumentResolver로 변경

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
