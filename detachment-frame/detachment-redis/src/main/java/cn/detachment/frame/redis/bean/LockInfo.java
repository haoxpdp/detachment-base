package cn.detachment.frame.redis.bean;

import lombok.Data;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/17 23:10
 */
@Data
public class LockInfo<T> {
    private String key;
    private T value;
    /**
     * 持锁时间
     */
    private Long expire;
    /**
     * 获取锁超时时间
     */
    private Long timeOut;
    private int count = 0;

}
