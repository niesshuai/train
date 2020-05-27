package nss.learning.train.java_base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Author niessh
 * @Date 10:29 2020/5/20
 * @Filename ${FILENAME}
 * @Description:
 **/
public class ThreadTests {

    public static ThreadTests threadTests;

    public synchronized void test(String param){
        System.out.println(param + " 同步test方法");
        Thread.yield();
    }

    public static class MyThread extends Thread{
        @Override
        public void run() {
//            threadTests.test(this.currentThread().getName());
            System.out.println(this.currentThread().getName() + "继承Thread类");
        }
    }
    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("实现runnable接口");
        }
    }

    public static class MyCallAble implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("实现callable的call方法");
            return 1;
        }
    }

    public static void main(String[] args) {
        Test test = new Test() {
            @Override
            public void test() {
                System.out.println("new 一个接口");
            }
        };
        test.test();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("new runnable");
            }
        }).start();
//        threadTests = new ThreadTests();
        System.out.println("当前线程为" + Thread.currentThread().getName() + "优先级为" + Thread.currentThread().getPriority());
//        MyThread mt = new MyThread();
////        mt.start();
//
//        Thread thread1 = new Thread(mt);
//        Thread thread2 = new Thread(mt);
//        Thread thread3 = new Thread(mt);
//        Runnable runnable = new MyRunnable();
//        Thread runnableThread = new Thread(runnable);
//        thread.setPriority(1);
//        thread1.setPriority(5);
//        thread.start();
//        thread1.start();
//        try {
//            thread1.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        thread2.start();
//        thread3.start();
        MyCallAble myCallAble = new MyCallAble();
        FutureTask<Integer> future = new FutureTask<Integer>(myCallAble);
        new Thread(future,"").start();
        try {

            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("main方法执行完毕");
//        runnableThread.start();
    }
    /**
     * 当前线程为main优先级为5
     * Thread-0
     * Thread-3
     * Thread-0继承Thread类
     * Thread-1继承Thread类
     * 实现runnable接口
     * Thread-2继承Thread类
     * **/
}
