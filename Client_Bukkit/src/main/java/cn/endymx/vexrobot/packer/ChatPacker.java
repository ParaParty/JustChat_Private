package cn.endymx.vexrobot.packer;

import cn.endymx.vexrobot.util.MessagePackType;
import cn.endymx.vexrobot.util.MessageTools;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatPacker extends Packer implements ISendable {

    public ChatPacker(AsyncPlayerChatEvent event){
        super(getMsg(event));
    }

    private static String getMsg(AsyncPlayerChatEvent event){
        JSONObject chatMessage = new JSONObject();
        chatMessage.put("version", PackVersion);
        chatMessage.put("type", MessagePackType.CHAT);
        chatMessage.put("world", MessageTools.Base64Encode(event.getPlayer().getWorld().getName()));
        chatMessage.put("world_display", MessageTools.Base64Encode(event.getPlayer().getWorld().getName()));
        chatMessage.put("sender", MessageTools.Base64Encode(event.getPlayer().getName()));
        Map<String, String> chatArray = new HashMap<>();
        chatArray.put("type", "text");
        chatArray.put("content",MessageTools.Base64Encode(event.getMessage()));
        chatMessage.put("content", new JSONArray().put(chatArray));
        return chatMessage.toString();
    }
}

