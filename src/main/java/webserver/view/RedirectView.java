package webserver.view;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.util.Map;

public class RedirectView implements View {

    private final String location;

    public RedirectView(String location) {
        this.location = location;
    }

    @Override
    public void render(Map<String, Object> model, HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.FOUND);
        response.setHeader("Location", location);
    }
}
