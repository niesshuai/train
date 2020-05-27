package nss.learning.train.java_base;

/**
 * @Author niessh
 * @Date 14:53 2020/5/25
 * @Filename ${FILENAME}
 * @Description:
 **/
public class ThreadLocalTest {

    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

    public static Integer num = 0;

    public void addNumber() {
        // 使用threadlocal对象，可使num字段为每个线程独有，多线程操作互不影响
        // 如果直接使用全局变量num进行操作，多线程操作的话会有影响
        threadLocal.set(num);
        Integer number = threadLocal.get();
        System.out.println("线程" + Thread.currentThread().getName() + "获取ThreadLocal值=" + number);
        System.out.println("线程" + Thread.currentThread().getName() + "获取num值=" + num);
        number ++;
        num ++;
        threadLocal.set(number);
        System.out.println("线程" + Thread.currentThread().getName() + "获取ThreadLocal++值=" + threadLocal.get());
        System.out.println("线程" + Thread.currentThread().getName() + "获取num++值=" + num);
        threadLocal.remove();
    }

    public static void main(String[] args) {
        ThreadLocalTest threadLocalTest = new ThreadLocalTest();
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalTest.addNumber();
                }
            }).start();
        }
    }
}
