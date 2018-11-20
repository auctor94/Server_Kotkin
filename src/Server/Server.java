package Server;
import DBConnect.ConnectSQL;

import Personnel.Personnel;
import com.sun.org.apache.xml.internal.res.XMLErrorResources_tr;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
                        //menuUser();
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

        }
         catch (SQLException e) {
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


    private void menuAdmin() throws IOException, ClassNotFoundException, SQLException {
        int i = 0;
        while (i == 0) {
            switch ((int) is.readObject()) {
                case 1111:
                    viewTableResult();
                    if (111300 == (int) is.readObject()) {
                        ConnectSQL connectSQL = new ConnectSQL();
                        int t = (int) is.readObject();
                        String reason = (String)is.readObject();
                        connectSQL.dismissRow(t,reason);
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
                case 1115:
                  //  diogramMark();
                    break;
                case 1116:
                  //  workWithAlternativ();
                    break;
                case 3333:
                    i = 1;

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
                            os.writeObject(resultSet.getInt("idPosition"));
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
                os.writeObject(resultSet.getInt("idPosition"));


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
                String name = (String) is.readObject();
                String pass = (String) is.readObject();

                ConnectSQL connectSQL = new ConnectSQL();
                if (connectSQL.registerNewUser(name, pass)) {
                    os.writeObject(1);
                } else
                    os.writeObject(0);
            } else if (swich == 3333)
                return;
        }
    }
}