package cn.endymx.vexrobot.packer;

import cn.endymx.vexrobot.util.MessagePackType;
import cn.endymx.vexrobot.util.MessageTools;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONObject;

public class ChatPacker extends Packer implements ISendable {
    public static int PackVersion = 2;

    public ChatPacker(AsyncPlayerChatEvent event){
        super(getMsg(event));
    }

    private static String getMsg(AsyncPlayerChatEvent event){
        JSONObject pingMessage = new JSONObject();
        pingMessage.put("version", PackVersion);
        pingMessage.put("type", MessagePackType.CHAT);
        pingMessage.put("world", MessageTools.Base64Encode(event.getPlayer().getWorld().getName()));
        pingMessage.put("world_display", MessageTools.Base64Encode(event.getPlayer().getWorld().getName()));
        pingMessage.put("sender", MessageTools.Base64Encode(event.getPlayer().getName()));
        pingMessage.put("content",MessageTools.Base64Encode(event.getMessage()));
        return pingMessage.toString();
    }
}

