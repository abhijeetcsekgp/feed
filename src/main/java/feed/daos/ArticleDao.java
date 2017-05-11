package feed.daos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.*;

@Component
public class ArticleDao {
    private static final String CREATE_ARTICLE_QUERY = "INSERT INTO article(title, url) VALUES(?, ?)";
    private static final String ADD_ARTICLE_TO_FEED_QUERY = "INSERT INTO feed_article(feed_id, article_id) VALUES(?, ?)";

    @Inject
    private ComboPooledDataSource dataSource;

    public Long createArticle(long feedId, String title, String url) {
        Connection connection = null;
        PreparedStatement createArticleStmt = null;
        PreparedStatement addArticleStmt = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            createArticleStmt = connection.prepareStatement(CREATE_ARTICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
            createArticleStmt.setString(1, title);
            createArticleStmt.setString(2, url);

            createArticleStmt.executeUpdate();
            ResultSet resultSet = createArticleStmt.getGeneratedKeys();
            long articleId = -1L;
            while (resultSet.next()) {
                articleId = resultSet.getLong(1);
            }

            addArticleStmt = connection.prepareStatement(ADD_ARTICLE_TO_FEED_QUERY);
            addArticleStmt.setLong(1, feedId);
            addArticleStmt.setLong(2, articleId);
            addArticleStmt.execute();

            connection.commit();

            return articleId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (createArticleStmt != null) {
                try {
                    createArticleStmt.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }

            if (addArticleStmt != null) {
                try {
                    addArticleStmt.close();
                } catch (SQLException e) {
                    //do nothing
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
        }
    }
}
