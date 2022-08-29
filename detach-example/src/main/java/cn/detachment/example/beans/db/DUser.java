package cn.detachment.example.beans.db;

import cn.detachment.mysql.helper.DetachDecrypt;
import cn.detachment.mysql.helper.DetachEncrypt;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @author haoxp
 */
@Data
@TableName("d_user")
public class DUser {
    @JSONField(serializeUsing = ToStringSerializer.class)
    protected Long id;


    private String name;
    @DetachEncrypt
    @DetachDecrypt
    private String phone;

}
