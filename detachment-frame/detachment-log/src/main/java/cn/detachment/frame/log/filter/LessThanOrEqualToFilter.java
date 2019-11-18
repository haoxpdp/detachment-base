package cn.detachment.frame.log.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author haoxp
 * @date 2019-10-21 18:20
 */
public class LessThanOrEqualToFilter extends Filter<ILoggingEvent> {

    Level level;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (event.getLevel().levelInt <= level.levelInt) {
            return FilterReply.NEUTRAL;
        } else {
            return FilterReply.DENY;
        }
    }

    public void setLevel(String level) {
        this.level = Level.toLevel(level);
    }

    @Override
    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}

