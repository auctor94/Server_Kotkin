package Charts;

import DBConnect.ConnectSQL;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HiringChart extends Chart implements Serializable {
    private LocalDate hire;
    private List<HiringChart> spisok;


    public HiringChart() {
        this.spisok = new ArrayList<>();
    }

    public HiringChart(LocalDate hire) {
        this.hire = hire;
    }

    public LocalDate getHire() {
        return hire;
    }

    public void setHire(LocalDate hire) {
        this.hire = hire;
    }

    public List<HiringChart> getSpisok() {
        return spisok;
    }

    public void setSpisok(List<HiringChart> spisok) {
        this.spisok = spisok;
    }

    @Override
    public void makeCharts() throws SQLException {
        this.spisok = new ArrayList<>();
        ConnectSQL connectSQL = new ConnectSQL();
        ResultSet salaries = connectSQL.getHireData();
        while (salaries.next())
        {

            this.spisok.add(new HiringChart(salaries.getDate("hireDate").toLocalDate()));
        }
    }

    public void push(HiringChart e) {
        spisok.add(e);
    }
}
