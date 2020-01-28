package cn.detachment.frame.core.constant;

import cn.detachment.frame.core.exception.UnsupportedSSLVersionException;
import lombok.Getter;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/1/28 16:54
 */
public enum SSLVersion {
    SSL("SSL"),
    SSLv3("SSLv3"),
    TLSv1("TLSv1"),
    TLSv1_1("TLSv1.1"),
    TLSv1_2("TLSv1.2");
    @Getter
    private String name;

    SSLVersion(String name) {
        this.name = name;
    }

    public static SSLVersion find(String name) {
        for (SSLVersion sl : SSLVersion.values()) {
            if (sl.getName().toUpperCase().equals(name.toUpperCase())) {
                return sl;
            }
        }
        throw new UnsupportedSSLVersionException("unsupported ssl version:" + name);
    }
}
