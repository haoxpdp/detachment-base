package cn.detachment.example.redis.service;

import cn.detachment.frame.redis.annotation.DetachLock;
import org.springframework.stereotype.Service;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/21 22:34
 */
@Service
public class LockTestService {
    private static int nano = 0;

    @DetachLock
    public void syncPlusNano() {
        nano++;
    }

    public int getNano() {
        return nano;
    }

}
