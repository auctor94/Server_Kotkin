package Charts;

import DBConnect.ConnectSQL;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SalaryChart extends Chart implements Serializable {
    private String surname;
    private String name;
    private String lastName;
    private Float salary;
    private List<SalaryChart> spisok;


    public SalaryChart() {
        this.spisok = new ArrayList<>();
    }

    public SalaryChart(String surname, String name, String lastName, Float salary) {
        this.surname = surname;
        this.name = name;
        this.lastName = lastName;
        this.salary = salary;
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
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public List<SalaryChart> getSpisok() {
        return spisok;
    }

    public void setSpisok(List<SalaryChart> spisok) {
        this.spisok = spisok;
    }

    @Override
    public void makeCharts() throws SQLException {
        this.spisok = new ArrayList<>();
        ConnectSQL connectSQL = new ConnectSQL();
        ResultSet salaries = connectSQL.getSalaryData();
        String data = null;
        while (salaries.next())
        {

            this.spisok.add(new SalaryChart(salaries.getString("surname"),salaries.getString("name"),salaries.getString("lastName"),salaries.getFloat("salary")));
        }
    }
    public void push(SalaryChart e)
    {
        spisok.add(e);
    }
}
