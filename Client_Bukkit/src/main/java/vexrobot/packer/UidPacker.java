package cn.endymx.vexrobot.packer;

import cn.endymx.vexrobot.util.MessagePackType;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import org.json.JSONObject;

public class UidPacker extends Packer implements ISendable {
    private static int PackVersion = 2;

    public UidPacker(){
        super(getMsg());
    }

    private static String getMsg(){
        JSONObject uidMessage = new JSONObject();
        uidMessage.put("version", PackVersion);
        uidMessage.put("type", MessagePackType.UID);
        uidMessage.put("uid", "mc");
        return uidMessage.toString();
    }
}
