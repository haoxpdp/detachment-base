package cn.detachment.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author haoxp
 * @date 20/9/16
 */
@Component
@ControllerAdvice
@ConditionalOnWebApplication
@ConditionalOnMissingBean(ExceptionHandler.class)
public class ExceptionHandler {

    public static final String PROD_ENV = "prod";

    @Value("spring.profiles.active:dev")
    private String currentEnv;


}
