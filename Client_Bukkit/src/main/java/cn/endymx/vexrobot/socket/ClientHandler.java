package cn.endymx.vexrobot.socket;

import cn.endymx.vexrobot.EncodeClass;
import cn.endymx.vexrobot.LoadClass;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private LoadClass plugin;
    private String os;

    ClientHandler(LoadClass plugin){
        this.plugin = plugin;
        if (Pattern.matches("Linux.*", System.getProperty("os.name"))) {
            os = "Linux";
        }else {
            os = "Windows";
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        new EncodeClass(os.equals("Linux") ? new String(result1, "GBK") : new String(result1), plugin).encodeData();
        result.release();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        plugin.getLogger().warning("与服务器丢失链接，关闭插件");
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
        String msg = "{\"type\":\"client\", \"system\":\"" + os + "\", \"name\":\"测试服务器\"}";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes("GBK"));
        ctx.write(encoded);
        ctx.flush();
    }
}