import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        }

        String url = "jdbc:mysql://localhost:3306/LibraryDB";
        String user = "root"; //  MySQL username
        String pass = "Antrin"; // MySQL password

        return DriverManager.getConnection(url, user, pass);
    }
}