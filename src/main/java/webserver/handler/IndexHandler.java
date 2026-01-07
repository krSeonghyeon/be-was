package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.router.Handler;
import webserver.view.TemplateRenderer;

public class IndexHandler implements Handler {

    private final TemplateRenderer renderer;

    public IndexHandler(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String html = renderer.render("index.html");

        response.setStatus(HttpStatus.OK);
        response.setHeader("Content-Type", "text/html");
        response.setBody(html.getBytes());
    }
}
