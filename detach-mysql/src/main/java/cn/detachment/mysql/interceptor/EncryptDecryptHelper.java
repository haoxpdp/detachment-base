package cn.detachment.mysql.interceptor;

import cn.detachment.mysql.helper.DetachDecrypt;
import cn.detachment.mysql.helper.DetachEncrypt;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author haoxp
 */
@Slf4j
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),

        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class EncryptDecryptHelper implements Interceptor, InitializingBean {

    @Value(value = "${crypto.keyword:25869eb3ff227d9e34b3512d3c3c92ed}")
    private String keyword;

    public static final String encryptFiledPrefix = Mode.ECB.name() + ":";
    AES aes;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof ParameterHandler) {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
            parameterField.setAccessible(true);
            Object parameterObject = parameterField.get(parameterHandler);

            if (Objects.nonNull(parameterObject)) {
                encryptField(parameterObject);
            }
            return invocation.proceed();
        }
        if (invocation.getTarget() instanceof ResultSetHandler) {
            Object obj = invocation.proceed();
            if (!(obj instanceof List)) {
                return invocation.proceed();
            }
            ((List<?>) obj).forEach(this::decryptField);
            return obj;
        }
        return invocation.proceed();
    }

    private void decryptField(Object obj) {
        Class<?> clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(DetachDecrypt.class) == null) {
                continue;
            }
            try {

                Object result = field.get(obj);
                String str = String.valueOf(result);
                if (str.startsWith(encryptFiledPrefix)) {
                    str = str.substring(encryptFiledPrefix.length());
                    field.set(obj, aes.decryptStr(str));
                }
            } catch (Exception e) {
                log.error("解密异常 ", e);
                log.error("脱敏字段解密异常 : {} {}", clz.getSimpleName(), field.getName());
            }
        }
    }

    private void encryptField(Object obj) throws IllegalAccessException {
        Class<?> clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(DetachEncrypt.class) == null || Objects.isNull(field.get(obj))) {
                continue;
            }
            Object o = field.get(obj);
            String val = String.valueOf(o);
            if (StringUtils.isEmpty(val)) continue;
            if (val.startsWith(encryptFiledPrefix)) continue;
            field.set(obj, encryptFiledPrefix + aes.encryptHex(val));
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public void afterPropertiesSet() {
        aes = new AES(Mode.ECB.name(), "pkcs7padding",
                HexUtil.decodeHex(keyword));
    }
}
