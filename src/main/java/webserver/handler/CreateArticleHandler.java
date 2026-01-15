package webserver.handler;

import db.ArticleDatabase;
import model.article.Article;
import webserver.exception.BadRequestException;
import webserver.http.HttpRequest;
import webserver.http.MultipartParser;
import webserver.http.MultipartResult;
import webserver.http.UploadFile;
import webserver.view.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateArticleHandler implements Handler {

    private final ArticleDatabase articleDatabase;

    public CreateArticleHandler(ArticleDatabase articleDatabase) {
        this.articleDatabase = articleDatabase;
    }

    public ModelAndView handle(HttpRequest request) {
        // TODO: 일단 내부에서 처리하도록 한뒤 추후 content-type에 따라 앞단에서 처리하도록 수정하기

        MultipartResult result = MultipartParser.parse(request);

        String content = result.fields().get("content");
        UploadFile image = result.files().get("image");

        String imageUrl = "";

        if (image == null || image.filename().isEmpty() || image.content().length == 0) {
            throw new BadRequestException("File not provided");
        } else {
            try {
                Files.write(Paths.get("uploads/" + image.filename()), image.content());
                imageUrl = "/uploads/" + image.filename();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("여기1");

        String newId = String.valueOf(articleDatabase.findAll().size() + 1);
        Article article = new Article(newId, content, imageUrl);
        System.out.println("여기2");
        articleDatabase.save(article);
        System.out.println("여기3");

        return new ModelAndView("redirect:/");
    }
}
