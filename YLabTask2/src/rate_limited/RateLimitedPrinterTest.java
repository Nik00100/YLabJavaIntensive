package rate_limited;

public class RateLimitedPrinterTest {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        RateLimitedPrinter rateLimitedPrinter = new RateLimitedPrinterImpl(1000);
        for (int i = 0; i < 10; i++) {
            rateLimitedPrinter.print(String.valueOf(i));
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Время работы программы " + duration + "мс");
    }
}
