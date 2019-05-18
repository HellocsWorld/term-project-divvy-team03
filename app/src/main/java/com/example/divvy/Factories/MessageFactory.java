package com.example.divvy.Factories;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.example.divvy.models.ImageMessage;
import com.example.divvy.models.Message;

import org.json.JSONException;
import org.json.JSONObject;


public class MessageFactory {

    public static Message create(JSONObject data){
        try {
            if (data.has("image")) {
                return new ImageMessage(data.getString("message"), data.getString("senderNickname"), data.getString("image"));
            }else{
                return new Message(data.getString("message"), data.getString("senderNickname"));
            }
        }catch(JSONException e){
            Log.d("ERROR: ", e.toString());
        }
        return null;
    }

    public static Message create(String messageText, String username, String bitmap){
        return new ImageMessage(messageText, username, bitmap);
    }

    public static Message create(String messageText, String username){
        return new Message(messageText, username);
    }





}
