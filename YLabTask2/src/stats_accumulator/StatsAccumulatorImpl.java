package stats_accumulator;

public class StatsAccumulatorImpl implements StatsAccumulator {
    private Integer maxValue;
    private Integer minValue;
    private int count;
    private double avg;

    public StatsAccumulatorImpl() {
        maxValue = null;
        minValue = null;
        count = 0;
        avg = 0;
    }

    @Override
    public void add(int value) {
        System.out.println("Добавлено значение " + value);
        count++;
        maxValue = count == 1 ? value : Math.max(maxValue, value);
        minValue = count == 1 ? value : Math.min(minValue, value);
        avg = (value + (count - 1) * avg) / count;
    }

    @Override
    public int getMin() {
        return minValue;
    }

    @Override
    public int getMax() {
        return maxValue;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return avg;
    }
}
