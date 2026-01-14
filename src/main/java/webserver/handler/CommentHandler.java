package webserver.handler;

import webserver.view.ModelAndView;

public class CommentHandler implements Handler {

    public ModelAndView handle() {
        return new ModelAndView("comment", null);
    }
}
