package nss.learning.train.java_base;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author niessh
 * @Date 18:36 2020/5/22
 * @Filename ${FILENAME}
 * @Description: 练习lock
 **/
public class LockTest {
    public void test(){
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取锁");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放锁");
        }
    }
    public static void main(String[] args) {
        LockTest lockTest = new LockTest();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lockTest.test();
                }
            }).start();
        }

    }
}
