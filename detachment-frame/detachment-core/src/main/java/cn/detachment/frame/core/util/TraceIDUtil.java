package cn.detachment.frame.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/11/29 11:49
 */
public class TraceIDUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraceIDUtil.class);

    private static final AtomicLong ATOMIC_LONG = new AtomicLong();

    static {
        String ip = IpUtil.getIp();
        LOGGER.info("ip={}", ip);
        if (StringUtils.hasText(ip)) {
            String[] split = ip.split("\\.");
            long ipNum = Long.parseLong(split[split.length - 1]);
            long currentTimeMillis = System.currentTimeMillis();

            currentTimeMillis = currentTimeMillis % 1000000000;

            ATOMIC_LONG
                    .set((long) (ipNum * Math.pow(10, Long.toString(currentTimeMillis).length()))
                            + currentTimeMillis);
            LOGGER.info("ATOMIC_LONG={}", ATOMIC_LONG.get());
        }
    }

    private TraceIDUtil() {
    }

    public static long incrementAndGet() {
        return ATOMIC_LONG.incrementAndGet();
    }
}
