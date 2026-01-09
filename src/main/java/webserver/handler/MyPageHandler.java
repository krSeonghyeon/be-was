package webserver.handler;

import webserver.view.ModelAndView;

public class MyPageHandler implements Handler {

    public ModelAndView handle() {
        return new ModelAndView("mypage", null);
    }
}
