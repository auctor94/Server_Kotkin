package Factory;

import Charts.Chart;

import java.sql.SQLException;

public abstract class ChartFactory {

    public void makeFactoryCharts() throws SQLException {

        Chart ChartforTab = createChart();
        ChartforTab.makeCharts();
    }

    public abstract Chart createChart();
}
