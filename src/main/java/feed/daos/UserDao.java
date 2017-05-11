package feed.daos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import feed.entities.Feed;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO _user(email_id) values(?)";
    private static final String READ_USER_QUERY = "SELECT id from _user where email_id = ?";
    private static final String SUBSCRIBE_USER_QUERY = "INSERT INTO subscription(user_id, feed_id) values(?, ?)";
    private static final String READ_SUBSCRIPTION_QUERY = "SELECT f.name from _user u, subscription s, feed f " +
            "where u.email_id = ? and u.id = s.user_id and s.feed_id = f.id";

    @Inject
    private ComboPooledDataSource dataSource;

    public void subscribeUser(long feedId, String emailId) {
        Connection connection = null;
        PreparedStatement readUserStmt = null;
        PreparedStatement createUserStmt = null;
        PreparedStatement subscribeUserStmt = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            long userId = -1L;

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
                }
            }

            subscribeUserStmt = connection.prepareStatement(SUBSCRIBE_USER_QUERY);
            subscribeUserStmt.setLong(1, userId);
            subscribeUserStmt.setLong(2, feedId);
            subscribeUserStmt.execute();

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public List<String> getSubscribedFeeds(String emailId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(READ_SUBSCRIPTION_QUERY);

            preparedStatement.setString(1, emailId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> feedList = new ArrayList<>();
            while (resultSet.next()) {
                feedList.add(resultSet.getString(1));
            }
            return feedList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
