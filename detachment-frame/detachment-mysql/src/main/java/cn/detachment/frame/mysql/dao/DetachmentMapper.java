package cn.detachment.frame.mysql.dao;

import cn.detachment.frame.mysql.bean.BaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/21 23:04
 */
public interface DetachmentMapper<T extends BaseEntity> extends BaseMapper<T> {

    /**
     * updateWithVersionByPrimaryKey
     * 根据id 和 版本号更新
     *
     * @param version version
     * @param record  record
     * @return int
     * @author haoxp
     * @date 19/10/21 23:08
     */
    int updateWithVersionByPrimaryKey(@Param("version") Integer version, @Param("record") T record);

}
