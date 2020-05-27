package nss.learning.train.java_base;

import java.util.concurrent.*;

/**
 * @Author niessh
 * @Date 16:00 2020/5/21
 * @Filename ${FILENAME}
 * @Description:
 **/
public class ThreadPoolTest {
    public static class MyTask implements Runnable{
        String tashName;
        public MyTask (int i) {
            this.tashName = "线程" + i;
        }
        @Override
        public void run() {
            System.out.println( tashName+"任务开始");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(tashName+"任务结束");
        }
    }
    /**
     *  execute方法没有返回结果
     * **/
    public static void executeTest() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,2000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5));
        for (int i = 0; i < 15; i++) {
            MyTask task = new MyTask(i);
            executor.execute(task);
            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
        }
        executor.shutdown();
    }
    /**
     *  submit方法是有返回结果的，通过Futre监测线程运行状态
     *  以及获取任务返回结果
     *  future.get()方法是阻塞的需要等待线程结束获得返回结果后才会返回，
     *  所以如果在for循环里使用，则线程池无法并行执行，而是串行执行
     * **/
    public static void submitTest(){
        Future[] futures = new Future[15];
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,2000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5));
        for (int i = 0; i < 15; i++) {
            MyTask task = new MyTask(i);
            futures[i] = executor.submit(task);
            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
        }

//        executor.shutdown();
        executor.shutdownNow();
        for (int i = 0; i < futures.length; i++) {
            try {
                System.out.println(futures[i].isCancelled());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        submitTest();
    }
}
