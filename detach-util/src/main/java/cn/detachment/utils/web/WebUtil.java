package cn.detachment.utils.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

/**
 * @author haoxp
 */
public class WebUtil {
    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst( "X-Forwarded-For" );
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
            ip = headers.getFirst( "Proxy-Client-IP" );
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
            ip = headers.getFirst( "WL-Proxy-Client-IP" );
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
            ip = headers.getFirst( "X-Real-IP" );
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        if (ip != null && ip.length() != 0 && ip.contains( "," )) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static String getHeader(ServerWebExchange exchange, String key) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        if (!headers.containsKey(key)) {
            return StringUtils.EMPTY;
        }

        List<String> values = headers.get(key);

        return CollectionUtils.isEmpty(values) ? StringUtils.EMPTY : values.get(0);
    }
}
