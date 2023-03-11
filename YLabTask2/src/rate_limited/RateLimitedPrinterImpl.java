package rate_limited;

public class RateLimitedPrinterImpl implements RateLimitedPrinter {
    private int interval;
    private long lastPrintTime;

    public RateLimitedPrinterImpl(int interval) {
        this.interval = interval;
    }

    @Override
    public void print(String message) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPrintTime >= interval) {
            System.out.println(message);
            lastPrintTime = currentTime;
        }
    }
}
