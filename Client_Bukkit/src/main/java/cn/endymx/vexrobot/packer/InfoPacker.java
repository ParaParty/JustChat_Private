package cn.endymx.vexrobot.packer;

import cn.endymx.vexrobot.util.MessagePackType;
import cn.endymx.vexrobot.util.MessageTools;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONObject;

public class InfoPacker extends Packer implements ISendable {
    public static int PackVersion = 1;

    public InfoPacker(AsyncPlayerChatEvent event, Player player){
        super(getMsg(event, player));
    }

    private static String getMsg(AsyncPlayerChatEvent event, Player player){
        JSONObject pingMessage = new JSONObject();
        pingMessage.put("version", PackVersion);
        pingMessage.put("type", MessagePackType.INFO);
        pingMessage.put("sender", MessageTools.Base64Encode(player.getName()));
        return pingMessage.toString();
    }
}
