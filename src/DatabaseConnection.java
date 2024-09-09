import java.sql.*;

public class DatabaseConnection {
private static final String URL = "jdbc:sqlserver://DESKTOP-G5OF0J3;Database=Empresa;IntegratedSecurity=True;trustServerCertificate=True";
private static final String USER = "root";
private static final String PASSWORD = "root";


    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
