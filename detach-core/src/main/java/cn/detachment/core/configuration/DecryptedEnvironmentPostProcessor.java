package cn.detachment.core.configuration;

import cn.detachment.core.util.AesUtil;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/6/4 23:12
 */
public class DecryptedEnvironmentPostProcessor implements EnvironmentPostProcessor {

    public static final String DECRYPT_PREFIX = "d_pwd:";

    public static final String KEY = "detachKey";

    @SneakyThrows
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String detachKey = null;
        for (PropertySource<?> ps : environment.getPropertySources()) {
            if (ps instanceof SimpleCommandLinePropertySource) {
                SimpleCommandLinePropertySource source = (SimpleCommandLinePropertySource) ps;
                detachKey = source.getProperty(KEY);
                break;
            }
        }
        if (StringUtils.isEmpty(detachKey)) {
            return;
        }
        HashMap<String, Object> decryptMap = new HashMap<>();

        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof OriginTrackedMapPropertySource) {
                OriginTrackedMapPropertySource source = (OriginTrackedMapPropertySource) propertySource;
                for (String key : source.getPropertyNames()) {
                    if (!(source.getProperty(key) instanceof String)) {
                        continue;
                    }
                    String v = (String) source.getProperty(key);
                    assert v != null;
                    if (!v.startsWith(DECRYPT_PREFIX)) {
                        continue;
                    }
                    decryptMap.put(key, AesUtil.decrypt(v.substring(DECRYPT_PREFIX.length()), detachKey));
                }

            }
            if (!CollectionUtils.isEmpty(decryptMap)) {
                environment.getPropertySources().addFirst(new MapPropertySource("custom-encrypt", decryptMap));
            }
        }


    }


}
