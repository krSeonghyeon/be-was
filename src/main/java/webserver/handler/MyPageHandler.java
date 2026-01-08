package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.router.Handler;
import webserver.view.TemplateRenderer;

public class MyPageHandler implements Handler {

    private final TemplateRenderer renderer;

    public MyPageHandler(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String html = renderer.render("mypage", null);
        
        response.setStatus(HttpStatus.OK);
        response.setHeader("Content-Type", "text/html");
        response.setBody(html.getBytes());
    }
}
