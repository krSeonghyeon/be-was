package webserver.handler;

import db.Database;
import model.article.Article;
import webserver.exception.BadRequestException;
import webserver.http.HttpRequest;
import webserver.http.MultipartParser;
import webserver.http.MultipartResult;
import webserver.http.UploadFile;
import webserver.view.ModelAndView;

public class CreateArticleHandler implements Handler {

    public ModelAndView handle(HttpRequest request) {
        // TODO: 일단 내부에서 처리하도록 한뒤 추후 content-type에 따라 앞단에서 처리하도록 수정하기

        MultipartResult result = MultipartParser.parse(request);

        String content = result.fields().get("content");
        UploadFile image = result.files().get("image");

        if (content == null) {
            throw new BadRequestException("Missing parameter");
        }

        // TODO: 이미지 저장 로직 추가하기

        String newId = String.valueOf(Database.findArticleAll().size() + 1);
        Article article = new Article(newId, content);
        Database.addArticle(article);

        return new ModelAndView("redirect:/");
    }
}
