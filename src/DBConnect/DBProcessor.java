package DBConnect;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBProcessor {
    private Connection connection;

    public DBProcessor() throws SQLException {
    }

    public Connection getConnection(String url, String username, String password) throws SQLException {
        if (connection != null)
            return connection;
        else {
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        }
    }
}
