package nss.learning.train.java_base;

import java.util.*;

/**
 * @Author niessh
 * @Date 15:45 2020/5/18
 * @Filename ${FILENAME}
 * @Description:
 **/
public class CollectionTests {
    public static void queueTest(){
        PriorityQueue queue = new PriorityQueue();
        queue.add("456");
        queue.add("abc");
        queue.add("123");
        Iterator iterator = queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(queue.poll());
        }
    }
    public static void linkedHashMapTest(){
        LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
        map.put("456", "123");
        map.put("123", "123");
        map.put("789", "123");
        System.out.println(map.keySet());
    }
    public static void hashMapTest(){
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("456", "123");
        map.put("123", "123");
        map.put("789", "123");
        System.out.println(map.keySet());
    }
    public static void treeMapTest(){
        TreeMap<String,String> map = new TreeMap<String, String>();
        map.put("456", "123");
        map.put("123", "123");
        map.put("789", "123");
        System.out.println(map.keySet());
    }
    static int MAXIMUM_CAPACITY = 1 << 30;
    public static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
//        queueTest();
//        System.out.println(tableSizeFor(7));
        linkedHashMapTest();
        hashMapTest();
        treeMapTest();
        hashMapTest();
    }
}
