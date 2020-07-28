package cn.detach.api.builder;

import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 18:09
 */
public class UrlBuilder {

    private String urlTemplate;

    int index = 0;

    Set<String> keys;

    private Method method;
    private Object[] args;

    private ParameterMapping mapping;

    public String build() {
        TokenParser parser = new TokenParser("${", "}", mapping);
        return parser.parseTemplate(this.urlTemplate);
    }

    public UrlBuilder(String url, Method method, Object[] args) {
        this.urlTemplate = StringUtils.trimWhitespace(url);
        this.mapping = new ParameterMapping(url, method.getParameters(), args);
    }

}
