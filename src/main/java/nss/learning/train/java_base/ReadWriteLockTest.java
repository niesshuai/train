package nss.learning.train.java_base;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author niessh
 * @Date 15:04 2020/5/23
 * @Filename ${FILENAME}
 * @Description:
 **/
public class ReadWriteLockTest {
    public ReadWriteLock lock = new ReentrantReadWriteLock();
    public int value = 1;
    public Lock read = lock.readLock();
    public Lock write = lock.writeLock();
    public void addWrite () {
        try {
            write.lock();
            System.out.println("主线程添加写锁");
            Thread.sleep(4000);
            value ++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            write.unlock();
            System.out.println("主线程释放写锁");
        }
    }
    public void getRead(){

        try {
            read.lock();
            Thread.sleep(1000);
            System.out.println("线程" + Thread.currentThread().getName() + "获取读锁");
            System.out.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            read.unlock();
            System.out.println("线程" + Thread.currentThread().getName() + "释放读锁");
        }
    }

    public static void main(String[] args) {

        ReadWriteLockTest readWriteLockTest = new ReadWriteLockTest();
//        readWriteLockTest.addWrite();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readWriteLockTest.getRead();
                }
            }).start();
        }
    }
}
