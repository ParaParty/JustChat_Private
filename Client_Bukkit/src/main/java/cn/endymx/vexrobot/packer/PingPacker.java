package cn.endymx.vexrobot.packer;

import cn.endymx.vexrobot.util.MessagePackType;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import org.json.JSONObject;

public class PingPacker extends Packer implements IPulseSendable {

    public PingPacker(){
        super(getMsg());
    }

    private static String getMsg(){
        JSONObject pingMessage = new JSONObject();
        pingMessage.put("version", PackVersion);
        pingMessage.put("type", MessagePackType.PING);
        return pingMessage.toString();
    }
}
