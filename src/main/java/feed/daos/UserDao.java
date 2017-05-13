package feed.daos;

import feed.entities.Article;
import feed.entities.Feed;
import feed.exception.AppException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status;

@Component
public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO _user(email_id) values(?)";
    private static final String READ_USER_QUERY = "SELECT id from _user where email_id = ?";
    private static final String SUBSCRIBE_USER_QUERY = "INSERT INTO subscription(user_id, feed_id) values(?, ?)";
    private static final String READ_SUBSCRIPTION_QUERY = "SELECT f.id, f.name, f.description " +
            "from _user u, subscription s, feed f " +
            "where u.email_id = ? and u.id = s.user_id and s.feed_id = f.id";
    private static final String READ_SUBSCRIBED_ARTICLES_QUERY = "select a.id, a.title, a.url from " +
            "_user u, subscription s, feed_article fa, article a " +
            "where u.email_id = ? and u.id = s.user_id and s.feed_id = fa.feed_id and fa.article_id = a.id";

    @Inject
    private DataSource dataSource;

    public void subscribeUser(long feedId, String emailId) {
        Connection connection = null;
        PreparedStatement readUserStmt = null;
        PreparedStatement createUserStmt = null;
        PreparedStatement subscribeUserStmt = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            long userId;
            readUserStmt = connection.prepareStatement(READ_USER_QUERY);
            readUserStmt.setString(1, emailId);

            ResultSet resultSet = readUserStmt.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getLong(1);
            } else {
                createUserStmt = connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
                createUserStmt.setString(1, emailId);
                createUserStmt.executeUpdate();
                ResultSet generatedKeys = createUserStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getLong(1);
                } else {
                    throw new AppException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Unknown error");
                }
            }

            subscribeUserStmt = connection.prepareStatement(SUBSCRIBE_USER_QUERY);
            subscribeUserStmt.setLong(1, userId);
            subscribeUserStmt.setLong(2, feedId);
            subscribeUserStmt.execute();

            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new AppException(Status.FORBIDDEN.getStatusCode(), "Already subscribed to feed");
        } catch (SQLException e) {
            throw new AppException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Unknown error");
        } finally {
            if (readUserStmt != null) {
                try {
                    readUserStmt.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }

            if (createUserStmt != null) {
                try {
                    createUserStmt.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }

            if (subscribeUserStmt != null) {
                try {
                    subscribeUserStmt.close();
                } catch (SQLException e) {
                    // do nothing
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

    public List<Feed> getSubscribedFeeds(String emailId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(READ_SUBSCRIPTION_QUERY);

            preparedStatement.setString(1, emailId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Feed> feedList = new ArrayList<>();
            while (resultSet.next()) {
                Feed feed = new Feed(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3));
                feedList.add(feed);
            }
            return feedList;
        } catch (SQLException e) {
            throw new AppException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Unknown error");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    // do nothing
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

    public List<Article> getSubscribedArticles(String emailId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(READ_SUBSCRIBED_ARTICLES_QUERY);

            preparedStatement.setString(1, emailId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Article> articleList = new ArrayList<>();
            while (resultSet.next()) {
                Article article = new Article(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3));
                articleList.add(article);
            }
            return articleList;
        } catch (SQLException e) {
            throw new AppException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Unknown error");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    // do nothing
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
