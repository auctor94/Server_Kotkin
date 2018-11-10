package Server;
import DBConnect.ConnectSQL;

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
                        //menuAdmin();
                        break;
                    case 222:
                        //menuUser();
                        break;
                    case 999:
                        //regist();
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

        } /*catch (SQLException e) {
            num--;
            e.printStackTrace();
        }*/
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
}
