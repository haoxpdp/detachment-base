package cn.detachment.example.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/11/29 14:37
 */
@Service
public class TestService {

    private static Logger logger = LoggerFactory.getLogger(TestService.class);

    public void testLog(){
        logger.info("test service !");
    }

}
