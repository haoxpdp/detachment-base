package cn.detachment.mysql.service;

import cn.detachment.mysql.SnowflakeIdWorker;
import cn.detachment.mysql.bean.BaseEntity;
import cn.detachment.mysql.dao.DetachmentMapper;
import cn.detachment.mysql.util.IpUtil;
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

    public boolean updateWithVersionById(T record) {
        Assert.notNull(record, "record must not be null!");
        int version = record.getOptimistic();
        record.setOptimistic(version + 1);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(T::getOptimistic, version)
                .eq(T::getId, record.getId());
        int updateCnt = baseMapper.update(record, queryWrapper);
        if (updateCnt > 0) {
            return true;
        }
        logger.warn("update with optimistic lock failed {} ", record);
        return false;
    }
}
