package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Name;

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


    //сделать кучу функций для таблицы, которую я создал!!!!!!!!!!!!!!!!!!!
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
        int id = 9;
        while (rs.next()) {
            id = rs.getInt("idUser");
            String nameUser = rs.getString("UserName");
            String passwordUser = rs.getString("UserPass");
            if (nameUser.equals(Name))//&& id > 0 && id < 10)
                return false;
        }
        if (id < 10) id = 10;
        else id++;


        String command = "INSERT INTO coursework.user (idUser, UserName, UserPass)  VALUE (" + id + ",\"" + Name.toString() + "\",\"" + Password.toString() + "\")";

        stmt.executeUpdate(command);

        stmt.close();
        rs.close();
        return true;

    }

    public static int getIdClient() {
        return idClient;
    }

    public ResultSet lookPersonnelALL() throws SQLException {
        String query = "SELECT * FROM coursework.personnel";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }
//добавить каскадное удаление!!!!!!!!!!!!!!!!!!!!!!
    public void deletRow(int t) throws SQLException {
        Statement statement = connection.createStatement();
        String deletRow = "DELETE FROM coursework.personnel where tabNumber = " + t + " ";
        System.out.println(statement.executeUpdate(deletRow));
    }

    public void updateSurname(int tabNum, String sur) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.personnel set surname = \"" + sur + "\" where tabNumber = " + tabNum + "";
        System.out.println(statement.executeUpdate(update));
    }

    public void updateName(int tabNum, String nam) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.personnel set name = \"" + nam + "\" where tabNumber = " + tabNum + "";
        System.out.println(statement.executeUpdate(update));
    }

    public void updateLastName(int tabNum, String last) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.personnel set lastName = \"" + last + "\" where tabNumber = " + tabNum + "";
        System.out.println(statement.executeUpdate(update));
    }

    public void updateEducation(int tabNum, String educ) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.personnel set education = \"" + educ + "\" where tabNumber = " + tabNum + "";
        System.out.println(statement.executeUpdate(update));
    }

    public void dismissRow(int t, String reason) throws SQLException {
        String query = "SELECT * FROM coursework.personnel where tabNumber = \"" + t + "\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
String FIO = null;
        LocalDate today =  LocalDate.now();
        while (resultSet.next()) {
            FIO = resultSet.getString("surname") ;
            FIO +=  " ";
            FIO += resultSet.getString("name");
            FIO += " ";
            FIO += resultSet.getString("lastName");
            System.out.println(FIO);
        }
        String deletRow = "DELETE FROM coursework.personnel where tabNumber = " + t + " ";
        System.out.println(statement.executeUpdate(deletRow));
        String command = "INSERT INTO coursework.dismiss (FIO, dismissDate, reasonDismiss)  VALUE (\"" + FIO + "\",\"" + today + "\",\"" + reason + "\")";
        System.out.println(statement.executeUpdate(command));
        statement.close();

    }

    public ResultSet loojHighTable() throws SQLException {
        String query = "SELECT personnel.surname, personnel.name, personnel.lastName, position.posName, wages.wagesSize, wages.wagesSize*wages.prizePercent/100 as prizeSize \n" +
                "from personnel\n" +
                "INNER JOIN position on idPosition = Position\n" +
                "INNER JOIN wages on Number = tabNumber";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public ResultSet loojMiddleTable() throws SQLException {
        String query = "SELECT CONCAT(personnel.surname,' ', personnel.name, ' ', personnel.lastName) as SURNAME, encouraging.encDescription, encouraging.encSize, encouraging.encMonth from personnel\n" +
                "INNER JOIN encouraging on tabNum = tabNumber";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public ResultSet loojLowTable() throws SQLException {
        String query = "SELECT CONCAT(personnel.surname,' ', personnel.name, ' ', personnel.lastName) as SURNAME, wages.prizePercent from personnel\n" +
            "INNER JOIN wages on Number = tabNumber";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public void updatePercent(int tab, String readObject) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.wages set prizePercent = \"" + readObject + "\" where Number = \"" + tab + "\"";
        System.out.println(statement.executeUpdate(update));
    }

    public int findTab(String line) throws SQLException {
        System.out.println(line);
        String query = "SELECT * FROM coursework.personnel where surname =\"" + line +"\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int ret = 0;
        while(resultSet.next()) {
            ret = resultSet.getInt("tabNumber");
        }
        return ret;

    }

    public void insertEncorage(int tabNum, float readObject, String readObject1, LocalDate readObject2) throws SQLException {
        System.out.println(tabNum);
        System.out.println(readObject);
        System.out.println(readObject1);
        System.out.println(readObject2.toString());
        String command = "INSERT INTO coursework.encouraging (encSize, encDescription, tabNum, encMonth)  VALUE (" + readObject + ",\"" + readObject1 + "\"," + tabNum +",\"" + readObject2.toString() + "\")";
        Statement statement = connection.createStatement();
        statement.execute(command);
        statement.close();
    }
}
