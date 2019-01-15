package cn.endymx.vexrobot.socket;

import cn.endymx.vexrobot.LoadClass;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {

    private int port;
    private String host;
    private LoadClass plugin;
    public SocketChannel socketChannel;

    public Client(int port, String host, LoadClass plugin) {
        this.port = port;
        this.host = host;
        this.plugin = plugin;
        start();
    }
    private void start(){
        ChannelFuture future;
        try {
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host,port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(new IdleStateHandler(20,10,0));
                    //socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                    //socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    socketChannel.pipeline().addLast(new ClientHandler(plugin));
                }
            });
            future = bootstrap.connect(host,port).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel)future.channel();
                plugin.getLogger().info("成功连接到服务器");
            }else{
                plugin.getLogger().info("连接失败");
                plugin.getLogger().info("准备重连");
                start();
            }
        } catch (Exception ignored) {

        }
    }
}
