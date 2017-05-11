package feed.daos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.*;

@Component
public class FeedDao {
    private static final String CREATE_FEED_QUERY = "INSERT INTO feed(name, description) values(?, ?);";

    @Inject
    private ComboPooledDataSource dataSource;

    public Long createFeed(String feedName, String description) {
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
                return generatedKeys.getLong(1);
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Entry already exists");
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

        return null;
    }
}
