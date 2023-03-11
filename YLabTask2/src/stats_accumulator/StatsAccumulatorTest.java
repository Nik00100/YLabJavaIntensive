package stats_accumulator;

public class StatsAccumulatorTest {
    public static void main(String[] args) {
        StatsAccumulator stat = new StatsAccumulatorImpl();

        stat.add(1);
        System.out.println("max " + stat.getMax());
        System.out.println("min " + stat.getMin());
        System.out.println("avg " + stat.getAvg());
        System.out.println("count " + stat.getCount());

        stat.add(2);
        System.out.println("max " + stat.getMax());
        System.out.println("min " + stat.getMin());
        System.out.println("avg " + stat.getAvg());
        System.out.println("count " + stat.getCount());

        stat.add(6);
        System.out.println("max " + stat.getMax());
        System.out.println("min " + stat.getMin());
        System.out.println("avg " + stat.getAvg());
        System.out.println("count " + stat.getCount());

        stat.add(-1);
        System.out.println("max " + stat.getMax());
        System.out.println("min " + stat.getMin());
        System.out.println("avg " + stat.getAvg());
        System.out.println("count " + stat.getCount());

    }
}
