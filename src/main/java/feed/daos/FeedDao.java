package feed.daos;

import feed.entities.Feed;
import feed.exception.AppException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.*;

import static javax.ws.rs.core.Response.Status;

@Component
public class FeedDao {
    private static final String CREATE_FEED_QUERY = "INSERT INTO feed(name, description) values(?, ?);";

    @Inject
    private DataSource dataSource;

    public Feed createFeed(String feedName, String description) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(CREATE_FEED_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, feedName);
            preparedStatement.setString(2, description);

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return new Feed(generatedKeys.getLong(1), feedName, description);
            } else {
                throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Unknown error");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new AppException(Status.FORBIDDEN.getStatusCode(), "Feed already exists");
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
                    //do nothing
                }
            }
        }
    }
}
