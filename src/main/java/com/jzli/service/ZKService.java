package com.jzli.service;

import com.alibaba.fastjson.JSONObject;
import com.jzli.util.ZooKeeperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * =======================================================
 *
 * @Company 金色家网络科技有限公司-云存储业务部
 * @Date ：2017/5/25
 * @Author ：li.jinzhao
 * @Version ：0.0.1
 * @Description ：
 * ========================================================
 */
@Service
public class ZKService {
    private static final Logger logger = LoggerFactory.getLogger(ZKService.class);

    /**
     * 获取自增序号
     *
     * @param path
     * @return
     */
    public String increase(String path) {
        String result = null;
        try {
            //判断节点是否存在,不存在则创建节点
            if (!exists(path)) {
                //创建节点，锁资源
                result = ZooKeeperUtils.increase(path, UUID.randomUUID().toString());
                if (!ObjectUtils.isEmpty(result)) {
                    result = result.replace("/", "");
                }
                unlock(result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 锁资源
     *
     * @param path
     * @return
     */
    public Boolean lock(String path) {
        try {
            //判断节点是否存在,不存在则创建节点
            if (!exists(path)) {
                //创建节点，锁资源
                ZooKeeperUtils.create(path, UUID.randomUUID().toString());
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    /**
     * 解锁
     *
     * @param path
     * @return
     */
    public Boolean unlock(String path) {
        try {
            //判断节点是否存在,存在则删除节点
            if (exists(path)) {
                //删除节点，解锁资源
                ZooKeeperUtils.delete(path);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    /**
     * 设置
     *
     * @param path
     * @param value
     * @return
     */
    public Boolean set(String path, Object value) {
        try {
            if (!ObjectUtils.isEmpty(value)) {
                //判断节点是否存在，不存在则创建节点
                if (!exists(path)) {
                    ZooKeeperUtils.create(path, value.toString());
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    /**
     * 设置
     *
     * @param jsonObject
     * @return
     */
    public Boolean set(JSONObject jsonObject) {
        if (!ObjectUtils.isEmpty(jsonObject)) {
            Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Boolean set = set(entry.getKey(), entry.getValue());
                if (!set) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 获取
     *
     * @param path
     * @return
     */
    public String get(String path) {
        try {
            //判断节点是否存在，存在则获取节点
            if (exists(path)) {
                return ZooKeeperUtils.getData(path);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 判断是否存在
     *
     * @param path
     * @return
     */
    public Boolean exists(String path) {
        try {
            return ZooKeeperUtils.exists(path);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }
}
