package cn.endymx.vexrobot;

import com.google.gson.*;

import java.util.HashMap;

public class EncodeClass {

    private String data;
    private LoadClass plugin;

    public EncodeClass(String data, LoadClass plugin) {
        this.data = data;
        this.plugin = plugin;
    }

    public void encodeData(){
        JsonObject json = (JsonObject) new JsonParser().parse(data);
        switch(json.get("type").getAsString()){
            case "ping":
                HashMap<String, String> map = new HashMap<>();
                map.put("type", "pong");
                Gson json2 = new Gson();
                plugin.sendMsg(json2.toJson(map));
                break;
            case "chat":
                //待添加配置文件 自定义信息格式
                plugin.getServer().broadcastMessage("<" + json.get("player").getAsString() + "> " + json.get("message").getAsString());
                break;
        }
    }
}
