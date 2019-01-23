package cn.endymx.vexrobot.packer;

import cn.endymx.vexrobot.util.MessagePackType;
import cn.endymx.vexrobot.util.MessageTools;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class InfoPacker extends Packer implements ISendable {
    public static int PackVersion = 2;

    public InfoPacker(String message){
        super(getMsg(message));
    }

    private static String getMsg(String message){
        JSONObject pingMessage = new JSONObject();
        pingMessage.put("version", PackVersion);
        pingMessage.put("type", MessagePackType.INFO);
        pingMessage.put("content",MessageTools.Base64Encode(message));
        return pingMessage.toString();
    }
}