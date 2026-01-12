package webserver.handler;

import webserver.view.ModelAndView;

public class ArticlePageHandler implements Handler {

    public ModelAndView handle() {
        return new ModelAndView("article", null);
    }
}
