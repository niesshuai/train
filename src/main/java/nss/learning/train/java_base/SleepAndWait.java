package nss.learning.train.java_base;

/**
 * @Author niessh
 * @Date 18:02 2020/5/20
 * @Filename ${FILENAME}
 * @Description:
 **/
public class SleepAndWait implements Runnable{
    int number = 10;
    /**
     *  当一个线程访问该对象的同步方法并占有对象锁时，不会影响其他线程访问该对象的非同步方法
     * **/
    public void test(){
        number += 200;
        System.out.println(number + "访问非同步方法");
    }

    /**
     *  如果有其他线程通过wait方法让出cpu权限和对象锁，子线程需要通过notify方法唤醒那些访问该对象处于等待中的线程去竞争对象锁
     * **/
    public synchronized void threadMethod(){
        System.out.println("进入子线程");
        number += 100;
        System.out.println(number);
//        this.notifyAll();
    }
    /**
     *  main方法中调用该方法，主线程sleep期间，该线程一直持有该对象sleepAndWait的锁，不释放，
     *  子线程无法获取sleepAndWait对象锁，所以无法执行threadMethod，
     *  所以结果是当该方法执行完了之后即number *=2 之后释放了sleepAndWait对象锁，子线程才能访问该sleepAndWait对象
     *  即结果为 number*2 + 100
     * **/
    public synchronized void sleepMethod(){
        try {
            System.out.println("主线程进入sleep");
            Thread.sleep(2000);
            System.out.println("主线程结束sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        number *= 2;
    }
    /**
     * main方法中调用该方法，主线程wait的时候，该线程释放了当前对象的对象锁，其他线程可以访问该对象并获取对象锁
     * 但是wait的线程需要通过其他线程唤醒，使用对象的notify()或者notifyAll()方法唤醒那些处于wait状态下的线程，
     * 使他们去竞争该对象的锁进行操作，要不然wait中的线程一直处在等待状态无法获取该对象锁
     * 所以程序中即使主线程调用了wait但是不影响子线程访问该对象的同步方法，所以执行的是 number += 100;
     * **/
    public synchronized void waitMethod(){
        try {
            System.out.println("主线程进入wait");
            this.wait();
            System.out.println("主线程结束wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        number *= 2;
    }
    /**
     * yield方法使当前线程从运行变为就绪重新去经常cpu权限，不释放对象锁，
     * 所以如果主线程执行yield，就算主线程没有重新获得cpu权限，子线程也无法获取该对象锁
     * 故程序先乘3再加100结果为 number*3 + 100
     * **/
    public synchronized  void yieldMethod(){
        System.out.println("主线程进入yield");
        Thread.yield();
        System.out.println("主线程结束yield");
        number *= 3;
    }
    @Override
    public void run() {
        threadMethod();
//        test();
    }

    public static void main(String[] args) {
        SleepAndWait sleepAndWait = new SleepAndWait();
        Thread thread = new Thread(sleepAndWait);
        thread.start();
//        sleepAndWait.sleepMethod();
//        sleepAndWait.waitMethod();
        sleepAndWait.yieldMethod();
    }
}
