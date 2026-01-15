package webserver.handler;

import db.ArticleDatabase;
import model.article.Article;
import model.user.User;
import webserver.argument.annotation.CurrentUser;
import webserver.view.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexHandler implements Handler {

    private final ArticleDatabase articleDatabase;

    public IndexHandler(ArticleDatabase articleDatabase) {
        this.articleDatabase = articleDatabase;
    }

    public ModelAndView handle(@CurrentUser User user) {
        Map<String, Object> model = new HashMap<>();

        List<Article> articles = articleDatabase.findAll();
        if (!articles.isEmpty()) {
            Article latest = articles.get(articles.size() - 1);
            model.put("imageUrl", latest.getImageUrl());
            model.put("content", latest.getContent());
        }

        if (user != null) {
            model.put("isLogin", true);
            model.put("username", user.getName());
        } else {
            model.put("isLogin", false);
        }

        return new ModelAndView("index.html", model);
    }
}
