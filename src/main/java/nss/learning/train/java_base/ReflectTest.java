package nss.learning.train.java_base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Author niessh
 * @Date 21:31 2020/5/24
 * @Filename ${FILENAME}
 * @Description:
 **/
public class ReflectTest {
    // 无参构造方法
    public ReflectTest(){};

    // 有参构造方法
    protected ReflectTest(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 私有的构造方法
    private ReflectTest(int id){
        this.id = id;
    }

    private int id;
    public String name;

    private void privateMethod(){
        System.out.println("私有方法");
    }
    public void publicMethod(){
        System.out.println("公有方法");
    }

    public static void main(String[] args) {
        // 一般情况可通过构造方法直接构造对象，通过对象调用方法
        ReflectTest reflectTest = new ReflectTest();
        // 通过反射获取class对象
        try {
            // 获取clazz对象
            Class clazz = Class.forName("nss.learning.train.java_base.ReflectTest");
            Class clazz1 = ReflectTest.class;
            Class clazz2 = reflectTest.getClass();

            // 获取构造函数Constructor对象
            /**
             * 通过Class对象获取类的构造方法,通过Constructor的newInstance创建一个实例对象
             * getConstructor不传参数是获取无参构造方法
             * 传参数的话可获取类的相应参数的有参构造方法
             * 当然前提是类中有此构造方法
             *
             * 通过getDeclaredConstructor获取私有构造方法
             * 获取的私有构造器需要setAccessible为true才可以使用
             * **/
            Constructor constructor =  clazz.getConstructor();
            ReflectTest reflectTest1 = (ReflectTest) constructor.newInstance();

            Constructor constructor2 =  clazz.getDeclaredConstructor(int.class, String.class);
            ReflectTest reflectTest2 = (ReflectTest) constructor2.newInstance(1,"helloworld");

            // 通过getDeclaredConstructor获取私有构造方法
            Constructor constructor3 =  clazz.getDeclaredConstructor(int.class);
            // 私有构造方法如果想使用newInstance构造对象需要调用setAccessible,使该构造方法可见
            constructor3.setAccessible(true);
            ReflectTest reflectTest3 = (ReflectTest) constructor3.newInstance(1);

            /**
             *  如果不知道有哪些构造方法，可以通过getConstructors方法获取所有公有的构造方法
             *  getDeclaredConstructors方法获取所有的构造方法，包括公有，私有，受保护以及默认
             *  getModifiers可知道当前修饰符，根据返回的结果使用Modifier.isPrivate的方法判断是pubic还是private
             *  Constructor的getParameterTypes可知道构造函数参数的类型
             * **/
            Constructor[] constructors = clazz.getConstructors();
            for (int i = 0; i < constructors.length; i++) {
                Constructor c = constructors[i];
                System.out.println("构造方法的修饰符=" + c.getModifiers());
                // 通过Modifier的isPrivate方法判断该修饰符是否为private
                System.out.println(Modifier.isPrivate(c.getModifiers())? "private" : "public");
                System.out.println(Modifier.isPublic(c.getModifiers())? "public" : "private");
                // 获取参数类型
                Class[] paramTypes = c.getParameterTypes();
            }
            Constructor[] privateConstructors = clazz.getDeclaredConstructors();
            for (int i = 0; i < privateConstructors.length; i++) {
                Constructor c = privateConstructors[i];
                System.out.println("构造方法的修饰符=" + c.getModifiers());
                // 通过Modifier的isPrivate方法判断该修饰符是否为private
                System.out.println(Modifier.isPrivate(c.getModifiers())? "private" : "public");
                System.out.println(Modifier.isPublic(c.getModifiers())? "public" : "private");
                // 获取参数类型
                Class[] paramTypes = c.getParameterTypes();
            }

            // 获取字段 Field对象
            /**
             *  通过获取Field对象来操作，其他与construcntor操作一样
             * **/
            // 知道属性名字
            Field field = clazz.getField("name");
            field.set(reflectTest,"test");
            Field field2 = clazz.getDeclaredField("id");
            field2.setAccessible(true);
            field2.set(reflectTest,2);
            // 不知道有哪些属性，则获取所有属性
            Field[] fields1 = clazz.getFields();
            Field[] fields2 = clazz.getDeclaredFields();
            int paramMod = field.getModifiers();
            Modifier.isPrivate(paramMod);

            // 获取方法 Method对象
            /**
             *  通过获取Method对象来操作，其他与construcntor操作一样
             * **/
            // 知道方法名字
            Method method = clazz.getMethod("publicMethod");
            Method method2 = clazz.getDeclaredMethod("privateMethod");
            // 不知道方法名字
            Method[] methods = clazz.getMethods();
            Method[] methods2 = clazz.getDeclaredMethods();
            int methodMod = method.getModifiers();
            Modifier.isPrivate(methodMod);
            // 获取方法参数类型
            method.getParameterTypes();
            // 设置私有方法可用
            method2.setAccessible(true);
            // 调用方法
            method.invoke(reflectTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
