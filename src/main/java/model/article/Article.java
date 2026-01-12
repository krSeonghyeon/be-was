package model.article;

public class Article {
    private String articleId;
    private String content;

    public Article(String articleId, String content) {
        this.articleId = articleId;
        this.content = content;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getContent() {
        return content;
    }
}
