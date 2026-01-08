package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.view.ModelAndView;

public class MyPageHandler implements Handler {

    @Override
    public ModelAndView handle(HttpRequest request, HttpResponse response) {
        return new ModelAndView("mypage", null);
    }
}
