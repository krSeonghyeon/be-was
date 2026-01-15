package model.article;

public class Article {
    private String articleId;
    private String content;
    private String imageUrl;

    public Article(String articleId, String content, String imageUrl) {
        this.articleId = articleId;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
