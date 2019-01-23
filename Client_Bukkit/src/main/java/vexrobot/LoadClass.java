package cn.endymx.vexrobot;

import cn.endymx.vexrobot.packer.ChatPacker;
import cn.endymx.vexrobot.packer.InfoPacker;
import cn.endymx.vexrobot.packer.UidPacker;
import cn.endymx.vexrobot.socket.SocketClient;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * @author endymx @ VexRobot Project
 */
public class LoadClass extends JavaPlugin implements Listener{

    private SocketClient client;
    private HashMap<String, String> map = new HashMap<>();

    public void onLoad(){
        //this.saveDefaultConfig();
        //config = this.getConfig();
    }

    public void onEnable() {
        getLogger().info("加载中...");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("欢迎使用本插件，当前版本v1.0.0");
        client = new SocketClient("127.0.0.1", 8282, this);
        client.run();
        client.clientManager.send(new UidPacker());
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
        client.clientManager.send(new ChatPacker(event));
        map.clear();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        map.put("type", "info");
        map.put("message", event.getJoinMessage());
        client.clientManager.send(new InfoPacker(event.getJoinMessage()));
        map.clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        map.put("type", "info");
        map.put("message", event.getQuitMessage());
        client.clientManager.send(new InfoPacker(event.getQuitMessage()));
        map.clear();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().toLowerCase().equals("sendmsg")){
            //
        }
        return true;
    }
}
