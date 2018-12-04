package Charts;

import DBConnect.ConnectSQL;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PercentChart extends Chart implements Serializable {
    private String surname;
    private String name;
    private String lastName;
    private Float percent;
    private List<PercentChart> spisok;


    public PercentChart() {
        this.spisok = new ArrayList<>();
    }

    public PercentChart(String surname, String name, String lastName, Float salary) {
        this.surname = surname;
        this.name = name;
        this.lastName = lastName;
        this.percent = salary;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Float getSalary() {
        return percent;
    }

    public void setSalary(Float salary) {
        this.percent = salary;
    }

    public List<PercentChart> getSpisok() {
        return spisok;
    }

    public void setSpisok(List<PercentChart> spisok) {
        this.spisok = spisok;
    }

    @Override
    public void makeCharts() throws SQLException {
        this.spisok = new ArrayList<>();
        ConnectSQL connectSQL = new ConnectSQL();
        ResultSet salaries = connectSQL.getPercentData();
        while (salaries.next())
        {

            this.spisok.add(new PercentChart(salaries.getString("surname"),salaries.getString("name"),salaries.getString("lastName"),salaries.getFloat("prizePercent")));
        }
    }

    public void push(PercentChart e) {
        spisok.add(e);
    }
}

