package nss.learning.train.java_base;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Set;

/**
 * @Author niessh
 * @Date 11:05 2020/5/27
 * @Filename ${FILENAME}
 * @Description:
 **/
public class NIOtest {
    public static final String path = NIOtest.class.getClassLoader().getResource("").getPath();

    /**
     * ByteBuffer
     * CharBuffer
     * ShortBuffer
     * IntBuffer
     * LongBuffer
     * FloatBuffer
     * DoubleBuffer
     * **/
    public static void bufferTest(){
        // 获取非直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("position = " + byteBuffer.position());
        System.out.println("limit = " + byteBuffer.limit());
        System.out.println("capacity = " + byteBuffer.capacity());
        /**
         * position = 0
         * limit = 1024
         * capacity = 1024
         * **/
        // 获取直接缓冲区
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
//        byteBuffer.put()
        String s1 = "helloworld";
        String s2 = "你好世界";
        byte[] b1 = s1.getBytes();
        byte[] b2 = s2.getBytes();
        System.out.println(Arrays.toString(b1));
        System.out.println(Arrays.toString(b2));
        System.out.println(new String());
        byteBuffer.put(b1);
        System.out.println("position = " + byteBuffer.position());
        System.out.println("limit = " + byteBuffer.limit());
        System.out.println("capacity = " + byteBuffer.capacity());
        /**
         * position = 10
         * limit = 1024
         * capacity = 1024
         * 输出结果表示put之后只有positoin位置变了，limit和capacity没有变
         * 表明position到limit之间的数据还是可以继续put
         * **/
        byteBuffer.put(b2);
        System.out.println("position = " + byteBuffer.position());
        System.out.println("limit = " + byteBuffer.limit());
        System.out.println("capacity = " + byteBuffer.capacity());
        /**
         * position = 22
         * limit = 1024
         * capacity = 1024
         * 输出结果表示继续put之后只有positoin位置变了，limit和capacity还是没有变
         * **/
        byteBuffer.put(100,(byte)100);
        System.out.println("put之后直接get=" + byteBuffer.get(100));
        System.out.println("position = " + byteBuffer.position());
        System.out.println("limit = " + byteBuffer.limit());
        System.out.println("capacity = " + byteBuffer.capacity());
        System.out.println(byteBuffer.get(100));
        /**
         * position = 22
         * limit = 1024
         * capacity = 1024
         * put指定下标的位置，赋值一个字节，那么position limit capacity都没有变，只是赋值了
         * **/
        byteBuffer.flip();//改变positon和limit位置
        System.out.println("position = " + byteBuffer.position());
        System.out.println("limit = " + byteBuffer.limit());
        System.out.println("capacity = " + byteBuffer.capacity());
        /**
         * flip之后limit的值变为22，之前put的100位置的字节无法进行有效读取
         * **/
        byte[] b3 = new byte[byteBuffer.limit()];
        byteBuffer.get(b3);
        System.out.println(new String(b3));
        // 表示byteBuffer还有多少可用
        int remaining = byteBuffer.remaining();
        // 为当前limit赋值，但是如果之后调用了flip方法limit还是会赋值为position
        byteBuffer.limit(10);
        // array()获取了当前数组中所有有效字节数组，包括刚才put到下标100的那个位置
        byte[] b4 = byteBuffer.array();
        System.out.println(new String(b4));
        // 为当前position位置做标记配合reset使用
//        byteBuffer.mark();
//        // 将positoin值变为直接做过标记的mark值
//        byteBuffer.reset();
//        // 分割缓冲区
//        byteBuffer.slice();
        // clear方法重新初始化了position limit capacity和mark的值,但是没有清除数组中的数据
        byteBuffer.clear();
        b4 = byteBuffer.array();
        System.out.println("clear之后=" + new String(b4));
    }

    /**
     * FileChannel： 用于文件的数据读写
     * DatagramChannel： 用于UDP的数据读写
     * SocketChannel： 用于TCP的数据读写，一般是客户端实现
     * ServerSocketChannel: 允许我们监听TCP链接请求，每个请求会创建会一个SocketChannel，一般是服务器实现
     * **/
    public static void fileChannelTest() {
        try {
            // 通过输入流获取channel
            FileInputStream fis = new FileInputStream(path + "1.txt");
            FileChannel inchannel = fis.getChannel();
            FileChannel inchannel1 = FileChannel.open(Paths.get(path.substring(1,path.length()) + "1.txt"), StandardOpenOption.READ);
//            FileChannel outchannel = new FileOutputStream(path + "2.txt").getChannel();
            // 也可以通过FileChannel的open方法
            FileChannel outchannel = FileChannel.open(Paths.get(path.substring(1,path.length()) + "2.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
//            channel.read(byteBuffer);
//            System.out.println(new String(byteBuffer.array()));
            while (inchannel.read(buffer) != -1) {
                // 读写转换，改变posiion和limit位置，从读变成写
                buffer.flip();
                System.out.println("buffer经过flip之后limit大小" + buffer.limit());
                outchannel.write(buffer);
                buffer.clear();
            }
            // 通过transferTo或transferFrom
            FileChannel outchannel2 = FileChannel.open(Paths.get(path.substring(1,path.length()) + "3.txt"), StandardOpenOption.WRITE, StandardOpenOption.READ,StandardOpenOption.CREATE);
            FileChannel outchannel3 = FileChannel.open(Paths.get(path.substring(1,path.length()) + "4.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
//            inchannel.transferTo(0,inchannel.size(),outchannel2);
            outchannel3.transferFrom(inchannel1,0,inchannel1.size());

            // 还可以通过内存映射的方式读写文件
            MappedByteBuffer inMappedBuf = inchannel1.map(FileChannel.MapMode.READ_ONLY,0,inchannel.size());
            MappedByteBuffer outMappedBuf = outchannel2.map(FileChannel.MapMode.READ_WRITE,0,inchannel.size());
            byte[] mapBytes = new byte[inMappedBuf.limit()];
            inMappedBuf.get(mapBytes);
            outMappedBuf.put(mapBytes);
            fis.close();
            inchannel.close();
            outchannel.close();
            outchannel2.close();
            outchannel3.close();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     *  socketChannel用于网络IO的读取
     * **/
    public static void scoketChannelTest(){
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
            socketChannel.configureBlocking(false);
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8888));
            serverSocketChannel.configureBlocking(false);// 设置为非阻塞
            //开启一个selector
            Selector selector = Selector.open();
            // 将服务端注册到selector中，入参为开启的selector和要监听的类型，类型为SelectionKey中的枚举
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            // 如果将注册多个事件，则将各枚举进行与操作即可，
//            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT|SelectionKey.OP_CONNECT);
//            serverSocketChannel.
            //可以通过selector的select方法获取当前selector中所有监听的事件已经就绪的个数
            while (selector.select() > 0) {
                // 获取当前selector中所有已经就绪的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 遍历就绪的事件进行处理
                for(SelectionKey selectionKey : selectionKeys){
                    //以下几个方法用来判断是否为某个状态
//                    selectionKey.isAcceptable();
//                    selectionKey.isConnectable();
//                    selectionKey.isReadable();
//                    selectionKey.isValid();
//                    selectionKey.isWritable();
                    if (selectionKey.isAcceptable()) {
                        // 获取客户端的socket
                        SocketChannel client = serverSocketChannel.accept();
                        // 设置client为非阻塞
                        client.configureBlocking(false);
                        // 注册client到selector
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        bufferTest();
        fileChannelTest();
    }
}
