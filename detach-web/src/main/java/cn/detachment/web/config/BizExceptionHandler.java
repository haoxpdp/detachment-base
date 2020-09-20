package cn.detachment.web.config;

import cn.detachment.web.bean.Result;
import cn.detachment.web.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author haoxp
 * @date 20/9/16
 */
@Component
@RestControllerAdvice
@ConditionalOnWebApplication
@ConditionalOnMissingBean(BizExceptionHandler.class)
public class BizExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);

    public static final String PROD_ENV = "prod";

    @Value("spring.profiles.active:dev")
    private String currentEnv;


    @ExceptionHandler(BizException.class)
    public Result<?> bizException(BizException e) {
        logger.error(e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
}
