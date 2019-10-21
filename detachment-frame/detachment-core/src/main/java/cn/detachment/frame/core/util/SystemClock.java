package cn.detachment.frame.core.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/19 23:51
 */
public enum SystemClock {
    // ====
    INSTANCE(1L, 1);

    private final AtomicLong now;

    SystemClock(long period, int corePoolSize) {
        this.now = new AtomicLong(System.currentTimeMillis());
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(corePoolSize, r -> {
            Thread t = new Thread(r, "system-clock");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    public long currentTimeMillis() {
        return now.get();
    }

}
