package db;

import model.article.Article;
import model.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findUserAll() {
        return users.values();
    }

    // 추후 분리
    private static Map<String, Article> articles = new HashMap<>();

    public static void addArticle(Article article) {
        articles.put(article.getArticleId(), article);
    }

    public static Article findArticleById(String articleId) {
        return articles.get(articleId);
    }

    public static Collection<Article> findArticleAll() {
        return articles.values();
    }
}
