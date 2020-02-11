package cn.detachment.frame.mysql.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/21 23:00
 */
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @JSONField(serializeUsing = ToStringSerializer.class)
    protected Long id;

    protected Integer optimistic;

    protected LocalDateTime createTime;

    protected LocalDateTime lastUpdateTime;
}
