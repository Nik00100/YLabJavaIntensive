package rate_limited;

public class RateLimitedPrinterImpl implements RateLimitedPrinter{
    private int interval;

    public RateLimitedPrinterImpl(int interval) {
        this.interval = interval;
    }

    @Override
    public void print(String message) throws InterruptedException {
        Thread.sleep(interval);
        System.out.println(message);
    }
}
