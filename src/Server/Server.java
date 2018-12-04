package Server;

import ChangeLogPass.User;
import Charts.HiringChart;
import Charts.PercentChart;
import Charts.SalaryChart;
import DBConnect.ConnectSQL;

import Personnel.Personnel;
import Reports.EncourageReport;
import Reports.FirstReport;
import Reports.SecondReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JRException;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Server extends Thread {

    Socket socket;
    ObjectInputStream is;
    ObjectOutputStream os;
    private static int num;

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(3128, 0, InetAddress.getByName("localhost"));
            System.out.println("Сервер начал работу");


            while (true) {
                new Server(server.accept());
            }
        } catch (Exception e) {
            System.out.println("Error!!");
        }
    }

    public Server(Socket s) {
        this.socket = s;
        this.num++;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run() {
        try {
            System.out.println("Пользователь № " + num);

//            int r[] = (int[]) is.readObject();
//            for (int i = 0; i < r.length; i++)
//                r[i] += 10;
//            os.writeObject(r);
            System.out.println("IP - " + socket.getInetAddress());
            System.out.println("Host - " + socket.getLocalPort());
            System.out.println("Время - " + (new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(Calendar.getInstance().getTime())));

            is = new ObjectInputStream(socket.getInputStream());
            os = new ObjectOutputStream(socket.getOutputStream());


            //String logPas[] = (String[]) is.readObject();


            while (true) {
                int t = (int) is.readObject();
                int swich = 0;
                if (t == 121212)
                    swich = checkClient();//int) is.readObject();//получаем ответ, кто же у нас пользователь
                else if (t == 3333)
                    swich = 3333;
                else
                    swich = 999;

                switch (swich) {
                    case 111:
                        menuAdmin();
                        break;
                    case 222:
                        menuUser();
                        break;
                    case 999:
                        regist();
                        break;
                    case 3333:
                        System.out.println("Пользователь окончил работу");
                        socket.close();
                        num--;
                        return;
                }
            }

            //socket.close();

        } catch (IOException e) {
            num--;
            System.out.println("Ошибка с соединением" + e);
        } catch (ClassNotFoundException e) {
            num--;
            System.out.println("Ошибка с соединением" + e);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    //метод для проверки того, кто у нас пользователь: админ, юзер или просто неправильный ввод
    private int checkClient() throws IOException, ClassNotFoundException {

        String logPas[] = (String[]) is.readObject();

        int result = 0;
        try {
            ConnectSQL connectSQL = new ConnectSQL();
            if (connectSQL.checkingAdmin(logPas[0], logPas[1])) {
                System.out.println("here i am");
                result = 111;
                os.writeObject(111);//это админ, посылаем сигнал 111
            } else if (connectSQL.checkingUser(logPas[0], logPas[1])) {
                os.writeObject(222);//это пользователь, посылаем сигнал 111
                result = 222;
            } else {
                // ErrorWindow.display("Ошибка", "Неверный ввод");
                os.writeObject(404);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //  is.close();
        //  os.close();
        return result;
    }


    private void menuAdmin() throws IOException, ClassNotFoundException, SQLException, JRException {
        int i = 0;
        while (i == 0) {
            switch ((int) is.readObject()) {
                case 1111:
                    viewTableResult();
                    if (111300 == (int) is.readObject()) {
                        ConnectSQL connectSQL = new ConnectSQL();
                        int t = (int) is.readObject();
                        String reason = (String) is.readObject();
                        connectSQL.dismissRow(t, reason);
                    }
                    break;

                case 1112:
                    viewTableResult();
                    if (3333 != (int) is.readObject())
                        System.out.println("Error");
                    break;

                case 1113:
                    viewTableResult();
                    if (111300 == (int) is.readObject()) {
                        ConnectSQL connectSQL = new ConnectSQL();
                        int t = (int) is.readObject();
                        connectSQL.deletRow(t);
                    }
                    break;
                case 1114:
                    tableFXML(1);
                    break;
                //Окно оперирования премиями
                case 1115:
                    prizeFXML(1);
                    break;
                case 1116:
                    reports();

                    break;
                case 1117:
                    charts();
                    break;
                case 1118:
                    changeUser();
                    break;
                case 3333:
                    i = 1;

            }
        }

    }

    private void menuUser() throws IOException, ClassNotFoundException, SQLException {


        int i = 0;
        do {
            switch ((int) is.readObject()) {
                case 2221: {
                    charts();

                }
                break;

                case 2222: {
                    try {
                        reports();
                    } catch (JRException e) {
                        e.printStackTrace();
                    }
                }
                break;
                case 2223: {
                    viewTableResult();
                    if (3333 != (int) is.readObject())
                        System.out.println("Error");
                }
                break;
                case 2224:
                    poisk();
                    break;
                default:
                    i = 1;
            }
        } while (i == 0);

    }

    private void charts() throws SQLException, IOException, ClassNotFoundException {
        while (true) {
            switch ((int) is.readObject()) {
                case 4444: {
                    SalaryChart chart1 = new SalaryChart();
                    chart1.makeCharts();
                    List<SalaryChart> forClient = chart1.getSpisok();
                    os.writeObject(forClient);
                    PercentChart chart2 = new PercentChart();
                    chart2.makeCharts();
                    List<PercentChart> forClient2 = chart2.getSpisok();
                    os.writeObject(forClient2);
                    HiringChart chart3 = new HiringChart();
                    chart3.makeCharts();
                    List<HiringChart> forClient3 = chart3.getSpisok();
                    os.writeObject(forClient3);
                }
                break;
                case 3333:
                    os.writeObject(ConnectSQL.getIdClient());
                    return;
            }
        }
    }

    private void poisk() throws SQLException, IOException, ClassNotFoundException {
        while (true) {
            ConnectSQL connectSQL = new ConnectSQL();
            switch ((int) is.readObject()) {
                case 9999: {
                    String name = (String)is.readObject();
                    String result = connectSQL.findName(name,1);
                    if (result == null) os.writeObject(0);
                    else {
                        os.writeObject(1);
os.writeObject(result);
                    }
                }
                break;
                case 9998: {
                    String name = (String)is.readObject();
                    String result = connectSQL.findName(name,2);
                    if (result == null) os.writeObject(0);
                    else {
                        os.writeObject(1);
                        os.writeObject(result);
                    }
                }
                break;
                case 9997: {
                    String name = (String)is.readObject();
                    String result = connectSQL.findName(name,3);
                    if (result == null) os.writeObject(0);
                    else {
                        os.writeObject(1);
                        os.writeObject(result);
                    }
                }
                break;
                case 9996: {
                    String name = (String)is.readObject();
                    String result = connectSQL.findName(name,4);
                    if (result == null) os.writeObject(0);
                    else {
                        os.writeObject(1);
                        os.writeObject(result);
                    }
                }
                break;
                case 9995: {
                    String name = (String)is.readObject();
                    System.out.println(name);
                    String result = connectSQL.findName(name,5);
                    if (result == null) os.writeObject(0);
                    else {
                        os.writeObject(1);
                        os.writeObject(result);
                    }
                }
                break;
                case 3333:
                    os.writeObject(ConnectSQL.getIdClient());
                    return;



            }

        }
    }


    private void changeUser() throws SQLException, IOException, ClassNotFoundException {
        while (true) {
            ConnectSQL connectSQL = new ConnectSQL();
            switch ((int) is.readObject()) {
                case 5555:
                    try {

                        ResultSet resultSet = null;
                        List<User> spisok = new ArrayList<>();
                        resultSet = connectSQL.lookUsersAll();
                        while (resultSet.next()) {
                            String id = String.valueOf(resultSet.getInt("idUser"));
                            String log = resultSet.getString("UserName");
                            String pass = resultSet.getString("UserPass");

                            spisok.add(new User(log, pass, id));
                        }
                        os.writeObject(spisok);////////////////////&&&&&&&&&&&&&  Почему???????????????????????????

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 5556:
                    switch ((int) is.readObject()) {
                        case 1:
                            User temp = (User) is.readObject();
                            connectSQL.updateLogin(Integer.valueOf(temp.getId()), (String) temp.getLogin());
                            break;
                        case 2:
                            User temp1 = (User) is.readObject();
                            connectSQL.updatePassword(Integer.valueOf(temp1.getId()), (String) temp1.getPassword());
                            break;
                        case 3:
                            User temp2 = (User) is.readObject();
                            connectSQL.updateLoginPassword(Integer.valueOf(temp2.getId()), (String) temp2.getLogin(), (String) temp2.getPassword());
                            break;

                    }
                    break;
                case 3333:
                    os.writeObject(ConnectSQL.getIdClient());
                    return;
            }


        }


    }

    private void reports() throws JRException, IOException, SQLException, ClassNotFoundException {

        while (true) {
            ConnectSQL connectSQL = new ConnectSQL();
            switch ((int) is.readObject()) {
                case 8881:
                    String pathreport = (String) is.readObject();
                    FirstReport first = new FirstReport();
                    first.create(pathreport, "E:\\Всё для уроков\\ПрогСП\\Course work 5 term\\Server_Kotkin\\src\\Cherry_Landscape.jrxml");
                    break;
                case 8882:
                    String pathreport1 = (String) is.readObject();
                    SecondReport first1 = new SecondReport();
                    first1.create(pathreport1, "E:\\Всё для уроков\\ПрогСП\\Course work 5 term\\Server_Kotkin\\src\\Dismiss_report.jrxml");
                    break;

                case 8883:
                    String pathreport2 = (String) is.readObject();
                    EncourageReport first2 = new EncourageReport();
                    first2.create(pathreport2, "E:\\Всё для уроков\\ПрогСП\\Course work 5 term\\Server_Kotkin\\src\\Encourage_report.jrxml");

                    break;
                case 8884:
                    ResultSet resultset = connectSQL.getFullReport();
                    String pathreport3 = (String) is.readObject();
                    pathreport3 += "\\Full_report.txt";
                    while (resultset.next()) {
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "IT-компания \"LaLaLand\"       Дата основания: 01 сентября 2015 года\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Штаб компании (чел.): " + String.valueOf(resultset.getInt("count1")) + "\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Количество должностей в компании: " + String.valueOf(resultset.getInt("count2")) + "\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Средний размер заработной платы (в руб.):: " + String.valueOf(resultset.getFloat("avg1")) + "\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Средний размер процента премии: " + String.valueOf(resultset.getFloat("avg2")) + "\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Минимальный размер заработной платы (в руб.): " + String.valueOf(resultset.getFloat("min1")) + "\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Максимальный размер заработной платы (в руб.): " + String.valueOf(resultset.getFloat("max1")) + "\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Количество выплаченных поощрительных премий сотрудникам: " + String.valueOf(resultset.getInt("count3")));
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                        appendUsingFileWriter(pathreport3, "Количество уволенных работников за время сузествования компании: " + String.valueOf(resultset.getInt("count4")) + "\n");
                        appendUsingFileWriter(pathreport3, "------------------------------------------------------------------------------------------------\n");
                    }
                    break;
                case 3333:
                    os.writeObject(ConnectSQL.getIdClient());
                    return;
            }


        }
    }


    private void prizeFXML(int i) throws IOException, ClassNotFoundException, SQLException {

        while (true) {
            ConnectSQL connectSQL = new ConnectSQL();
            switch ((int) is.readObject()) {
                case 7777:
                    try {
                        String FIO = null;
                        ResultSet resultSet = null;
                        if (ConnectSQL.getIdClient() > 0 && ConnectSQL.getIdClient() < 10)
                            resultSet = connectSQL.loojHighTable();
                        else if (ConnectSQL.getIdClient() > 9 && ConnectSQL.getIdClient() < 100)
                            resultSet = connectSQL.loojHighTable();
                        while (resultSet.next()) {
                            os.writeObject(0);
                            //собрать ФИО в один string
                            FIO = resultSet.getString("surname");
                            FIO += " ";
                            FIO += resultSet.getString("name");
                            FIO += " ";
                            FIO += resultSet.getString("lastName");
                            os.writeObject(FIO);
                            os.writeObject(resultSet.getString("posName"));
                            os.writeObject(resultSet.getFloat("wagesSize"));
                            os.writeObject(resultSet.getFloat("prizeSize"));
                        }
                        os.writeObject(ConnectSQL.getIdClient());////////////////////&&&&&&&&&&&&&  Почему???????????????????????????

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7778:
                    try {
                        ResultSet resultSet = null;
                        if (ConnectSQL.getIdClient() > 0 && ConnectSQL.getIdClient() < 10)
                            resultSet = connectSQL.loojMiddleTable();
                        else if (ConnectSQL.getIdClient() > 9 && ConnectSQL.getIdClient() < 100)
                            resultSet = connectSQL.loojMiddleTable();
                        while (resultSet.next()) {
                            os.writeObject(0);
                            os.writeObject(resultSet.getString("SURNAME"));
                            os.writeObject(resultSet.getString("encDescription"));
                            os.writeObject(resultSet.getFloat("encSize"));
                            os.writeObject(resultSet.getDate("encMonth").toLocalDate());
                        }
                        os.writeObject(ConnectSQL.getIdClient());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 7779:
                    try {
                        ResultSet resultSet = null;
                        if (ConnectSQL.getIdClient() > 0 && ConnectSQL.getIdClient() < 10)
                            resultSet = connectSQL.loojLowTable();
                        else if (ConnectSQL.getIdClient() > 9 && ConnectSQL.getIdClient() < 100)
                            resultSet = connectSQL.loojLowTable();
                        while (resultSet.next()) {
                            os.writeObject(0);
                            os.writeObject(resultSet.getString("SURNAME"));
                            os.writeObject(resultSet.getFloat("prizePercent"));
                        }
                        os.writeObject(ConnectSQL.getIdClient());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3663:
                    switch ((int) is.readObject()) {
                        case 1:
                            String lines = (String) is.readObject();
                            String line[] = lines.split(" ");
                            int tab = connectSQL.findTab(line[0]);
                            connectSQL.updatePercent(tab, (String) is.readObject());

                            break;
                        case 2:
                            String surname = (String) is.readObject();
                            int tabNum = connectSQL.findTab(surname);
                            if (tabNum == 0) {
                                os.writeObject(0);
                                break;
                            } else {
                                os.writeObject(1);
                                connectSQL.insertEncorage(tabNum, Float.valueOf((String) is.readObject()), (String) is.readObject(), (LocalDate) is.readObject());
                            }
                            break;
                    }
                    break;
                case 3333:
                    os.writeObject(ConnectSQL.getIdClient());
                    return;
            }


        }
    }

    private void tableFXML(int i) throws IOException, ClassNotFoundException, SQLException {
        while (true) {
            ConnectSQL connectSQL = new ConnectSQL();
            switch ((int) is.readObject()) {
                case 3223:
                    try {

                        ResultSet resultSet = null;
                        if (ConnectSQL.getIdClient() > 0 && ConnectSQL.getIdClient() < 10)
                            resultSet = connectSQL.lookPersonnelALL();
                        else if (ConnectSQL.getIdClient() > 9 && ConnectSQL.getIdClient() < 100)
                            resultSet = connectSQL.lookPersonnelALL();
                        while (resultSet.next()) {
                            os.writeObject(0);
                            os.writeObject(resultSet.getInt("tabNumber"));
                            os.writeObject(resultSet.getString("surname"));
                            os.writeObject(resultSet.getString("name"));
                            os.writeObject(resultSet.getString("lastName"));
                            os.writeObject(resultSet.getDate("birthday").toLocalDate());
                            os.writeObject(resultSet.getString("education"));
                            os.writeObject(resultSet.getDate("hireDate").toLocalDate());
                            os.writeObject(resultSet.getInt("Position"));
                        }
                        os.writeObject(ConnectSQL.getIdClient());////////////////////&&&&&&&&&&&&&  Почему???????????????????????????

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3443:
                    connectSQL.deletRow((int) is.readObject());
                    break;

                case 3663:
                    switch ((int) is.readObject()) {
                        case 1:
                            connectSQL.updateSurname((int) is.readObject(), (String) is.readObject());
                            break;
                        case 2:
                            connectSQL.updateName((int) is.readObject(), (String) is.readObject());
                            break;
                        case 3:
                            connectSQL.updateLastName((int) is.readObject(), (String) is.readObject());
                            break;
                        case 4:
                            connectSQL.updateEducation((int) is.readObject(), (String) is.readObject());
                            break;
                    }
                    break;

                case 3553:
/////////////////////////////////
                    break;
                case 3333:
                    os.writeObject(ConnectSQL.getIdClient());
                    return;
            }


        }

    }

    private void viewTableResult() throws IOException {
        ObservableList<Personnel> buildings = FXCollections.observableArrayList();
        try {
            ConnectSQL connectSQL = new ConnectSQL();
            ResultSet resultSet = null;
            if (ConnectSQL.getIdClient() > 0 && ConnectSQL.getIdClient() < 10)
                resultSet = connectSQL.lookPersonnelALL();
            else if (ConnectSQL.getIdClient() > 9 && ConnectSQL.getIdClient() < 100)
                resultSet = connectSQL.lookPersonnelALL();
//тут нужно менять, если я хочу чтобы рахные пользователи видели разную информацию
            while (resultSet.next()) {

                os.writeObject(0);

                os.writeObject(resultSet.getInt("tabNumber"));
                os.writeObject(resultSet.getString("surname"));
                os.writeObject(resultSet.getString("name"));
                os.writeObject(resultSet.getString("lastName"));
                os.writeObject(resultSet.getDate("birthday").toLocalDate());
                os.writeObject(resultSet.getString("education"));
                os.writeObject(resultSet.getDate("hireDate").toLocalDate());
                os.writeObject(resultSet.getInt("Position"));


            }
            os.writeObject(ConnectSQL.getIdClient());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regist() throws IOException, ClassNotFoundException, SQLException {

        while (true) {
            int swich = (int) is.readObject();
            if (swich == 888) {
                int isAdmin = (int) is.readObject();

                String name = (String) is.readObject();
                String pass = (String) is.readObject();

                ConnectSQL connectSQL = new ConnectSQL();
                if (connectSQL.registerNewUser(name, pass, isAdmin)) {
                    os.writeObject(1);
                } else
                    os.writeObject(0);
            } else if (swich == 3333)
                return;
        }
    }

    private static void appendUsingFileWriter(String filePath, String text) {
        File file = new File(filePath);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file, true);
            fr.write(text);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
