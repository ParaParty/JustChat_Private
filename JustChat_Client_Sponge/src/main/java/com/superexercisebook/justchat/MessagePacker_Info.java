package com.superexercisebook.justchat;

import org.json.JSONException;
import org.json.JSONObject;

import org.spongepowered.api.entity.living.player.Player;

public class MessagePacker_Info extends MessagePacker{

    public MessagePacker_Info(int eventType,Player player){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PackVersion);
            jsonObject.put("type", MessagePackType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public MessagePacker_Info(int eventType,Player player,String content){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PackVersion);
            jsonObject.put("type", MessagePackType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            jsonObject.put("content", MessageTools.Base64Encode(content));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}

