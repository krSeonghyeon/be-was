package webserver.view;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.util.Map;

public class HtmlView implements View {

    private final String viewName;
    private final TemplateRenderer renderer;

    public HtmlView(String viewName, TemplateRenderer renderer) {
        this.viewName = viewName;
        this.renderer = renderer;
    }

    @Override
    public void render(Map<String, Object> model, HttpRequest request, HttpResponse response) {
        String html = renderer.render(viewName, model);

        if (html == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            return;
        }

        response.setStatus(HttpStatus.OK);
        response.setHeader("Content-Type", "text/html");
        response.setBody(html.getBytes());
    }
}
