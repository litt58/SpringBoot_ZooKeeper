package com.jzli.zookeeper;

import org.apache.zookeeper.*;

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
public class ZKConnectTest implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("10.10.36.17:2181", 5000, new ZKConnectTest());
        countDownLatch.await();
        System.out.println(zooKeeper.getState().toString());
//        zooKeeper.create("/bbb", "aaa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        byte[] data = zooKeeper.getData("/aaa", Boolean.TRUE, null);
        System.out.println(new String(data));
        TimeUnit.SECONDS.sleep(60);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("ZK state:" + event.getState().name());
        if (Event.KeeperState.SyncConnected.equals(event.getState())) {
            countDownLatch.countDown();
        }
    }
}
