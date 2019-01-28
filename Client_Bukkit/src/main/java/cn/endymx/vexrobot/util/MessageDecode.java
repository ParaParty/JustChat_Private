package cn.endymx.vexrobot.util;

import cn.endymx.vexrobot.LoadClass;
import cn.endymx.vexrobot.packer.Packer;
import org.json.JSONArray;
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
            if (json.getInt("version") == Packer.PackVersion) {
                switch(json.getInt("type")){
                    case MessagePackType.PING:
                        if(plugin.client.clientManager != null){
                            //喂狗
                            plugin.client.clientManager.getPulseManager().feed();
                        }
                        break;
                    case MessagePackType.INFO:
                        plugin.getServer().broadcastMessage(json.getString("content"));
                        break;
                    case MessagePackType.CHAT:
                        String world = MessageTools.Base64Decode(json.getString("world_display"));
                        String sender = MessageTools.Base64Decode(json.getString("sender"));
                        StringBuilder SContent = new StringBuilder();
                        JSONArray mjson = json.getJSONArray("content");
                        for (int i = 0; i < mjson.length(); i ++) {
                            JSONObject msg = mjson.getJSONObject(i);
                            switch(msg.getString("type")){
                                case "text":
                                    SContent.append(MessageTools.Base64Decode(msg.getString("content")));
                                    break;
                                case "cqcode":
                                    switch(msg.getString("function")){
                                        case "CQ:at":
                                            SContent.append(MessageTools.Base64Decode(msg.getString("target")));
                                            break;
                                        case "CQ:image":
                                            SContent.append(MessageTools.Base64Decode(msg.getString("content")));
                                            break;
                                    }
                                    break;
                            }
                        }
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
