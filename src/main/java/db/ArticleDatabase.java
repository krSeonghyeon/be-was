package db;

import model.article.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDatabase {

    public void save(Article article) {
        String sql = """
                INSERT INTO ARTICLES (article_id, content, image_url)
                VALUES (?, ?, ?)
                """;

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, article.getArticleId());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getImageUrl());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Article findById(String articleId) {
        String sql = """
                SELECT article_id, content, image_url
                FROM ARTICLES
                WHERE article_id = ?
                """;

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, articleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Article(
                        rs.getString("article_id"),
                        rs.getString("content"),
                        rs.getString("image_url")
                );
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Article> findAll() {
        String sql = """
                SELECT article_id, content, image_url
                FROM ARTICLES
                """;

        List<Article> articles = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getString("article_id"),
                        rs.getString("content"),
                        rs.getString("image_url")
                );
                articles.add(article);
            }

            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
