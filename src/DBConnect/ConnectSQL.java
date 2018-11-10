package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectSQL {

    private static final String user = "root";
    private static final String password = "root";
    private static final String url = "jdbc:mysql://localhost:3306/CourseWork?useUnicode=true&characterEncoding=utf8&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;

    private static int idClient;

    public ConnectSQL() throws SQLException {
        DBProcessor db = new DBProcessor();
        connection = db.getConnection(url, user, password);
    }

    public boolean checkingAdmin(String Name, String Password) throws SQLException {

        String query = "select * from coursework.user WHERE idUser >0 AND idUser <100";
        stmt = connection.createStatement();
        rs = stmt.executeQuery(query);

        while (rs.next()) {
            idClient = rs.getInt("idUser");
            String nameUser = rs.getString("UserName");
            String passwordUser = rs.getString("UserPass");
            if (nameUser.equals(Name) && passwordUser.equals(Password))//&& id > 0 && id < 10)
                return true;
        }
        stmt.close();
        return false;
    }

    public boolean checkingUser(String Name, String Password) throws SQLException {

        String query = "select * from coursework.user WHERE idUser >0 AND idUser <100";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            idClient = resultSet.getInt("idUser");
            String nameUser = resultSet.getString("UserName");
            String passwordUser = resultSet.getString("UserPass");
            if (nameUser.equals(Name) && passwordUser.equals(Password))// && id >= 10 && id < 100)
                return true;
        }
        statement.close();
        return false;
    }


    public boolean registerNewUser(String Name, String Password) throws SQLException {

        String query = "select * from coursework.user ";
        stmt = connection.createStatement();
        rs = stmt.executeQuery(query);
        int id;
        while (rs.next()) {
            id = rs.getInt("idUser");
            String nameUser = rs.getString("UserName");
            String passwordUser = rs.getString("UserPass");
            if (nameUser.equals(Name))//&& id > 0 && id < 10)
                return false;
        }



        String command = "INSERT INTO coursework.user (UserName, UserPass)  VALUE (\"" + Name.toString() + "\",\"" + Password.toString() + "\")";

        stmt.executeUpdate(command);

        stmt.close();
        rs.close();
        return true;

    }


}
