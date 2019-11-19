package cn.detachment.frame.web.filter;

import cn.detachment.frame.core.util.SystemClock;
import cn.detachment.frame.web.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/11/19 23:23
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "webServerTraceFilter", urlPatterns = "/*", asyncSupported = true)
public class WebServerTraceFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(WebServerTraceFilter.class);

    public static final String TRACE_ID = "TRACE_ID";

    @Value("${detachment.web.trace.enabled:true}")
    private boolean requestTraceEnabled;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            long start = SystemClock.INSTANCE.currentTimeMillis();
            MDC.put(TRACE_ID, Long.toString(start));
            long end = SystemClock.INSTANCE.currentTimeMillis();
            logTrace(httpServletRequest, httpServletResponse, end - start);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }

    private void logTrace(HttpServletRequest request, HttpServletResponse response, long time) {
        if (requestTraceEnabled) {
            logger.info("{} {} {} {} {} {}", NetworkUtil.getIpAddress(request),
                    request.getMethod(), request.getRequestURI(), request.getProtocol(),
                    response.getStatus(), time);
        }
    }
}
