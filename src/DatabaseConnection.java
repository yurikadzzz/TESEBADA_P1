import java.sql.*;

public class DatabaseConnection {
private static final String URL = "jdbc:sqlserver://25.3.89.213;Database=Empresa;trustServerCertificate=True";
private static final String USER = "ADM";
private static final String PASSWORD = "ADM";


    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
