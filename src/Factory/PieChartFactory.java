package Factory;

import Charts.Chart;
import Charts.HiringChart;

public class PieChartFactory extends ChartFactory {
    @Override
    public Chart createChart() {
        return new HiringChart();
    }
}
