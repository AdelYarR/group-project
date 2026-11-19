package academy.benchmark;

final class ProgressLoggerReaper {

    private static final String LOGGER_CLASS = "academy.logger.ProgressLogger";

    private ProgressLoggerReaper() {}

    static void reap() {
        Thread[] all = new Thread[Thread.activeCount() * 2 + 10];
        int n = Thread.enumerate(all);
        for (int i = 0; i < n; i++) {
            Thread t = all[i];
            if (t == null || !t.isAlive()) continue;
            if (!LOGGER_CLASS.equals(t.getClass().getName())) continue;

            t.interrupt();
            try {
                t.join(2_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
