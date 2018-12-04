package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


public class ConnectSQL {

    private static final String user = "root";
    private static final String password = "root";
    private static final String url = "jdbc:mysql://localhost:3306/CourseWork?useUnicode=true&characterEncoding=utf8&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public static Connection getConnection() {
        return connection;
    }

    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;

    private static int idClient;

    public ConnectSQL() throws SQLException {
        DBProcessor db = new DBProcessor();
        connection = db.getConnection(url, user, password);
    }

    public boolean checkingAdmin(String Name, String Password) throws SQLException {

        String query = "select * from coursework.user WHERE idUser >0 AND idUser <10";
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

        String query = "select * from coursework.user WHERE idUser >9 AND idUser <100";
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

    public boolean registerNewUser(String Name, String Password, int isAdmin) throws SQLException {

        String query = "select * from coursework.user ";
        stmt = connection.createStatement();
        rs = stmt.executeQuery(query);
        int counter1 = 0, counter2 =0;
        int id = 9;//cлучайное число

        while (rs.next()) {
            id = rs.getInt("idUser");
            if (id < 10) counter1++;
            String nameUser = rs.getString("UserName");
            String passwordUser = rs.getString("UserPass");
            if (nameUser.equals(Name))//&& id > 0 && id < 10)
                return false;
            counter2 = id;
        }

        if (counter1 == 9 && isAdmin == 1) {
            return false;
        }
        if (counter1 <9 && isAdmin == 1) {
            String command = "INSERT INTO coursework.user (idUser, UserName, UserPass)  VALUE (" + (counter1+1) + ",\"" + Name.toString() + "\",\"" + Password.toString() + "\")";

            stmt.executeUpdate(command);
        }
        if (counter2 > counter1 && counter2>9 && isAdmin == 0)
        {
            String command = "INSERT INTO coursework.user (idUser, UserName, UserPass)  VALUE (" + (counter2+1) + ",\"" + Name.toString() + "\",\"" + Password.toString() + "\")";

            stmt.executeUpdate(command);
        }







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
        LocalDate today = LocalDate.now();
        while (resultSet.next()) {
            FIO = resultSet.getString("surname");
            FIO += " ";
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
        String query = "SELECT * FROM coursework.personnel where surname =\"" + line + "\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int ret = 0;
        while (resultSet.next()) {
            ret = resultSet.getInt("tabNumber");
        }
        return ret;

    }

    public void insertEncorage(int tabNum, float readObject, String readObject1, LocalDate readObject2) throws SQLException {
        String command = "INSERT INTO coursework.encouraging (encSize, encDescription, tabNum, encMonth)  VALUE (" + readObject + ",\"" + readObject1 + "\"," + tabNum + ",\"" + readObject2.toString() + "\")";
        Statement statement = connection.createStatement();
        statement.execute(command);
        statement.close();
    }

    public ResultSet getSalaryData() throws SQLException {
        String query = "SELECT personnel.surname, personnel.name, personnel.lastName, wages.wagesSize+(wages.wagesSize*wages.prizePercent/100) as salary \n" +
                "from personnel\n" +
                "INNER JOIN wages on Number = tabNumber";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public ResultSet getPercentData() throws SQLException {
        String query = "SELECT personnel.surname, personnel.name, personnel.lastName, wages.prizePercent from personnel\n" +
                "INNER JOIN wages on Number = tabNumber";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public ResultSet getHireData() throws SQLException {
        String query = "SELECT personnel.hireDate from personnel";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public ResultSet getFullReport() throws SQLException {
        String query = "SELECT (SELECT COUNT(coursework.personnel.`tabNumber` ) from personnel) as count1, (SELECT COUNT( coursework.position.`posName` ) from position) as count2, AVG( wagesSize) as avg1 ,AVG( prizePercent) as avg2 , MIN( wagesSize) as min1,MAX( wagesSize) as max1,(SELECT COUNT( coursework.encouraging.`encSize` ) from encouraging) as count3,(SELECT COUNT( coursework.dismiss.`FIO` ) from dismiss) as count4 FROM coursework.personnel,coursework.position,coursework.wages, coursework.encouraging,coursework.dismiss";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public ResultSet lookUsersAll() throws SQLException {
        String query = "SELECT * FROM coursework.user";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public void updateLogin(Integer valueOf, String login) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.user set UserName = \"" + login + "\" where idUser = " + valueOf + "";
        System.out.println(statement.executeUpdate(update));
    }

    public void updatePassword(Integer valueOf, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.user set UserPass = \"" + password + "\" where idUser = " + valueOf + "";
        System.out.println(statement.executeUpdate(update));
    }

    public void updateLoginPassword(Integer valueOf, String login, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String update = "update coursework.user set UserName = \"" + login + "\", UserPass = \"" + password + "\" where idUser = " + valueOf + "";
        System.out.println(statement.executeUpdate(update));
    }
}
