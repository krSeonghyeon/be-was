package webserver.handler;

import model.user.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.router.Handler;
import webserver.view.TemplateRenderer;

import java.util.HashMap;
import java.util.Map;

public class IndexHandler implements Handler {

    private final TemplateRenderer renderer;

    public IndexHandler(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {

        // biz logic
        User user = (User)request.getAttribute("USER"); // 나중에 argumentResolver로 변경

        // 추후 ModelandView 형태로 리팩토링 필요
        Map<String, Object> model = new HashMap<>();

        if (user != null) {
            model.put("isLogin", true);
            model.put("username", user.getName());
        } else {
            model.put("isLogin", false);
        }

        String html = renderer.render("index.html", model);

        response.setStatus(HttpStatus.OK);
        response.setHeader("Content-Type", "text/html");
        response.setBody(html.getBytes());
    }
}
