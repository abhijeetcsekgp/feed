package feed.daos;

import feed.entities.Article;
import feed.exception.AppException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;

import static javax.ws.rs.core.Response.Status;

@Component
public class ArticleDao {
    private static final String CREATE_ARTICLE_QUERY = "INSERT INTO article(title, url) VALUES(?, ?)";
    private static final String ADD_ARTICLE_TO_FEED_QUERY = "INSERT INTO feed_article(feed_id, article_id) VALUES(?, ?)";

    @Inject
    private DataSource dataSource;

    public Article createArticle(long feedId, String title, String url) {
        Connection connection = null;
        PreparedStatement createArticleStmt = null;
        PreparedStatement addArticleStmt = null;
        try {
            connection = dataSource.getConnection();
            //create article and add to feed in a transaction
            connection.setAutoCommit(false);

            createArticleStmt = connection.prepareStatement(CREATE_ARTICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
            createArticleStmt.setString(1, title);
            createArticleStmt.setString(2, url);

            createArticleStmt.executeUpdate();
            ResultSet resultSet = createArticleStmt.getGeneratedKeys();
            long articleId;
            if (resultSet.next()) {
                //retrieve the generated id for the created article
                articleId = resultSet.getLong(1);
            } else {
                throw new AppException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Unknown error");
            }

            addArticleStmt = connection.prepareStatement(ADD_ARTICLE_TO_FEED_QUERY);
            addArticleStmt.setLong(1, feedId);
            addArticleStmt.setLong(2, articleId);
            addArticleStmt.execute();

            connection.commit();

            return new Article(articleId, title, url);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new AppException(Status.BAD_REQUEST.getStatusCode(), "Feed doesn't exist");
        } catch (SQLException e) {
            throw new AppException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Unknown error");
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
