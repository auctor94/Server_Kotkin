package Factory;

import Charts.Chart;
import Charts.PercentChart;

public class BarChartFactory extends ChartFactory {
    @Override
    public Chart createChart() {
        return new PercentChart();
    }
}
