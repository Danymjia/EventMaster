import java.sql.*;

public class conexionBD {

    private static final String url = "jdbc:mysql://localhost:3306/eventMaster";
    private static final String username = "root";
    private static final String password = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
