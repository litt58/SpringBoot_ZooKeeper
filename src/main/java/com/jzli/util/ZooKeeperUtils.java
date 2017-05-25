package com.jzli.util;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * =======================================================
 *
 * @Company 金色家网络科技有限公司-云存储业务部
 * @Date ：2017/5/24
 * @Author ：li.jinzhao
 * @Version ：0.0.1
 * @Description ：
 * ========================================================
 */
@Component
public class ZooKeeperUtils implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperUtils.class);

    private static String zkAddress;
    private static ZooKeeper zooKeeper;
    private static CountDownLatch latch = null;
    private final static String charset = "UTF-8";

    // 解析application.yml
    private static RelaxedPropertyResolver propResolver;

    public static synchronized String getZkAddress() {
        if (ObjectUtils.isEmpty(zkAddress)) {
            zkAddress = propResolver.getProperty("address");
        }
        return zkAddress;
    }

    public static synchronized ZooKeeper getInstance() {
        if (ObjectUtils.isEmpty(zooKeeper)) {
            zooKeeper = build();
        }
        return zooKeeper;
    }

    public static synchronized ZooKeeper build() {
        latch = new CountDownLatch(1);
        try {
            zooKeeper = new ZooKeeper(getZkAddress(), 5000, new SessionWatcher());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            latch = null;
        }

        return zooKeeper;
    }

    private static class SessionWatcher implements Watcher {
        public void process(WatchedEvent event) {
            if (event.getState() == Event.KeeperState.SyncConnected) {
                if (!ObjectUtils.isEmpty(latch)) {
                    latch.countDown();
                }
            } else if (event.getState() == Event.KeeperState.Expired) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    zooKeeper = null;
                    build();
                }
            }
        }
    }

    /**
     * 创建节点
     *
     * @param path
     * @param value
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void create(String path, String value) throws UnsupportedEncodingException, KeeperException, InterruptedException {
        getInstance().create(getPath(path), value.getBytes(charset), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 创建节点
     *
     * @param path
     * @param value
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void set(String path, String value) throws UnsupportedEncodingException, KeeperException, InterruptedException {
        getInstance().setData(getPath(path), value.getBytes(), -1);
    }

    /**
     * 获取节点自增
     *
     * @param path
     * @param value
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static String increase(String path, String value) throws UnsupportedEncodingException, KeeperException, InterruptedException {
        String result = getInstance().create(getPath(path), value.getBytes(charset), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        return result;
    }

    /**
     * 获取节点的值
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static String getData(String path) throws KeeperException, InterruptedException {
        byte[] data = getInstance().getData(getPath(path), Boolean.TRUE, null);
        return new String(data);
    }

    /**
     * 删除节点
     *
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void delete(String path) throws KeeperException, InterruptedException {
        getInstance().delete(getPath(path), -1);
    }

    /**
     * 检查节点是否可用
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static Boolean exists(String path) throws KeeperException, InterruptedException {
        Stat exists = getInstance().exists(getPath(path), Boolean.TRUE);
        if (!ObjectUtils.isEmpty(exists)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 解析路径，判断是否以“/”开头
     *
     * @param path
     * @return
     */
    public static String getPath(String path) {
        StringBuilder builder = new StringBuilder();
        if (!path.startsWith("/")) {
            builder.append("/");
        }
        builder.append(path);
        return builder.toString();
    }

    public void setEnvironment(Environment environment) {
        propResolver = new RelaxedPropertyResolver(environment, "spring.zookeeper.");
    }
}
