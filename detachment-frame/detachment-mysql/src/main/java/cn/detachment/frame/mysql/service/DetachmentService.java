package cn.detachment.frame.mysql.service;

import cn.detachment.frame.core.util.IpUtil;
import cn.detachment.frame.core.util.SnowflakeIdWorker;
import cn.detachment.frame.mysql.bean.BaseEntity;
import cn.detachment.frame.mysql.dao.DetachmentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/21 23:09
 */
public abstract class DetachmentService<M extends DetachmentMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

    protected static Logger logger = LoggerFactory.getLogger(DetachmentService.class);

    protected static final SnowflakeIdWorker ID_MAKER;

    static {
        String ip = IpUtil.getIp();
        int id = Integer.parseInt(ip.split("\\.")[3]) % 32;
        ID_MAKER = new SnowflakeIdWorker(id, Integer.parseInt("0"));
    }

    @Override
    public boolean save(T entity) {
        entity.setId(ID_MAKER.nextId());
        if (null == entity.getOptimistic()) {
            entity.setOptimistic(0);
        }
        return this.retBool(this.baseMapper.insert(entity));
    }

    public int updateWithVersionByPrimaryKey(Integer version, T record) {
        Assert.notNull(version, "version must not be null!");
        Assert.notNull(record, "record must not be null!");
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(T::getOptimistic, version)
                .eq(T::getId, record.getId());
        return baseMapper.update(record, queryWrapper);
    }
}
