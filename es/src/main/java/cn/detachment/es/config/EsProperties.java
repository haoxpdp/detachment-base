package cn.detachment.es.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/31 23:17
 */
@ConfigurationProperties(prefix = "detach-es.properties")
public class EsProperties {

    private String userName;

    private String password;


    private Integer socketTimeout = 3000;

    private Integer requestTimeout = -1;


    private HttpHost[] hosts;

    private Integer connectTimeout = 3000;

    public void setHttpHostString(String[] httpHostString) {
        if (httpHostString == null || httpHostString.length == 0) {
            throw new IllegalArgumentException("bad es client hosts");
        }
        hosts = new HttpHost[httpHostString.length];
        for (int i = 0; i < httpHostString.length; i++) {
            String s = httpHostString[i];
            String[] slice = s.split(":");
            HttpHost httpHost = new HttpHost(slice[0], Integer.parseInt(slice[1]));
            hosts[i] = httpHost;
        }
    }


    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public HttpHost[] getHosts() {
        return hosts;
    }
}
