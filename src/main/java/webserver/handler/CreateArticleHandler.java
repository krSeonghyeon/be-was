package webserver.handler;

import db.Database;
import model.article.Article;
import webserver.exception.BadRequestException;
import webserver.http.HttpRequest;
import webserver.http.QueryStringParser;
import webserver.view.ModelAndView;

import java.util.Map;

public class CreateArticleHandler implements Handler {

    public ModelAndView handle(HttpRequest request) {
        String body = new String(request.getBody());
        Map<String, String[]> params = QueryStringParser.parse(body);

        String content = getFirst(params, "content");

        if (content == null) {
            throw new BadRequestException("Missing parameter");
        }

        String newId = String.valueOf(Database.findArticleAll().size() + 1);
        Article article = new Article(newId, content);
        Database.addArticle(article);

        return new ModelAndView("redirect:/");
    }

    private String getFirst(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0) ? null : values[0];
    }
}
