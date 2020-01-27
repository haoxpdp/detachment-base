package cn.detachment.example.core.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/1/10 16:55
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 打开serverSocketChannel, 用于监听客户端链接，所有客户端链接的父管道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定监听端口，设置非阻塞模式
        serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getByName("IP"),99900));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
//        new Thread(new ReacotrTask()).start();
    }
}
