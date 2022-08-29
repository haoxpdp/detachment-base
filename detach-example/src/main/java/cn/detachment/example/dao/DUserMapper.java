package cn.detachment.example.dao;

import cn.detachment.example.beans.db.DUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haoxp
 */
@Mapper
public interface DUserMapper extends BaseMapper<DUser> {
}
