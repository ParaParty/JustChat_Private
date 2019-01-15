package cn.endymx.vexrobot.util;

import cn.endymx.vexrobot.LoadClass;
import cn.endymx.vexrobot.packer.ChatPacker;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageDecode {
    private String data;
    private LoadClass plugin;

    public  MessageDecode(String data, LoadClass plugin) {
        this.data = data;
        this.plugin = plugin;
    }

    public void decodeData(){
        try{
            JSONObject json = new JSONObject(data);
            int version = json.getInt("version");
            if (version == ChatPacker.PackVersion) {
                switch(json.getInt("type")){
                    case MessagePackType.PING:
                        break;
                    case MessagePackType.INFO:
                        break;
                    case MessagePackType.CHAT:
                        String world = MessageTools.Base64Decode(json.getString("world"));
                        String sender = MessageTools.Base64Decode(json.getString("sender"));
                        String SContent = MessageTools.Base64Decode(json.getString("content"));
                        plugin.getServer().broadcastMessage("[" + world + "]" + "<" + sender + "> " + SContent);//测试
                        break;
                    default:
                        plugin.getLogger().info("收到类型无法识别的消息");
                        break;
                }
            }else {
                plugin.getLogger().info("收到不同版本的消息");
            }
        }catch(JSONException e) {
            plugin.getLogger().warning("收到无法解析的信息");
        }
    }
}
