import java.sql.*;

public class TestConn {

    public static void main(String[] args) {
        String URL = "jdbc:sqlserver://DESKTOP-G5OF0J3;Database=Empresa;IntegratedSecurity=True;trustServerCertificate=True";
        String user = "root";
        String pw = "root";

        try {
            try(Connection conn = DriverManager.getConnection(URL, user, pw)) {
                System.out.println("Conexion exitosa!!!");
            }
        }
        catch (SQLException e) {
            System.out.println("Error! D:");
            e.printStackTrace();
        }
    }
}
