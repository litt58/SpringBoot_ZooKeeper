package com.jzli.zookeeper;

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
public class ArrayTest {
    public static void main(String[] args) {
        String[] ss = {"a", "b", "c"};
        String[] clone = ss.clone();
        for (int i = 0; i < clone.length; i++) {
            clone[i] = Integer.toString(i);
            System.out.println(clone[i]);
        }

        for (int i = 0; i < ss.length; i++) {
            System.out.println(ss[i]);
        }
    }
}
