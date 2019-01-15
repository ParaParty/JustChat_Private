package cn.endymx.vexrobot;

import cn.endymx.vexrobot.packer.ChatPacker;
import cn.endymx.vexrobot.socket.Client;
import cn.endymx.vexrobot.socket.SocketClient;

import com.google.gson.Gson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author endymx @ VexRobot Project
 */
public class LoadClass extends JavaPlugin implements Listener{

    private Client bootstrap;
    private SocketClient client;
    private HashMap<String, String> map = new HashMap<>();
    private Gson json = new Gson();

    public void onLoad(){
        //this.saveDefaultConfig();
        //config = this.getConfig();
    }

    public void onEnable() {
        getLogger().info("加载中...");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("欢迎使用本插件，当前版本v1.0.0");
        //bootstrap = new Client(23333, "sh2.endymx.cn", this);
        client = new SocketClient(23333, "sh2.endymx.cn", this);
        client.run();
        for(Plugin plugin : getServer().getPluginManager().getPlugins()){
            plugin.getLogger().info(plugin.getName());
            if(plugin.getName().equals("VexView")){
                getLogger().info("检查到VexView插件，启用图片功能");
                break;
            }
        }
    }

    public void onDisable() {
        getLogger().info("关闭中...");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        map.put("type", "chat");
        map.put("world", event.getPlayer().getWorld().getName());
        map.put("player", event.getPlayer().getName());
        map.put("message", event.getMessage());
        //sendMsg(json.toJson(map));
        client.clientManager.send(new ChatPacker(event));
        map.clear();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        map.put("type", "info");
        map.put("message", event.getJoinMessage());
        sendMsg(json.toJson(map));
        map.clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        map.put("type", "info");
        map.put("message", event.getQuitMessage());
        sendMsg(json.toJson(map));
        map.clear();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().toLowerCase().equals("sendmsg")){
            sendMsg(args[0]);
        }
        return true;
    }

    void sendMsg(String msg){
        ByteBuf encoded = bootstrap.socketChannel.alloc().buffer(4 * msg.length());
        try {
            encoded.writeBytes(msg.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bootstrap.socketChannel.writeAndFlush(encoded);
    }
}
