package com.example.divvy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.example.divvy.models.Listing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.divvy.httprequest.get;

public class GetListingsService extends IntentService {
    public GetListingsService(){
        super("getlistings");
    }
    @Override
    protected void onHandleIntent( @Nullable Intent intent) {
        Bundle bundle = new Bundle();
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        try {
            String data = get((HashMap<String, String>) intent.getSerializableExtra("data"),intent.getStringExtra("uri"));
            System.err.println(data);
            ArrayList<Listing> listings = convertDataToListings(data);
            bundle.putSerializable("data", listings);
            receiver.send(1, bundle);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Listing> convertDataToListings(String s) throws JSONException {
        ArrayList<Listing> listings = new ArrayList<>();
        JSONArray array = new JSONArray(s);
        for(int i = 0; i < array.length();i++){
            JSONObject jsonObject = (JSONObject)array.get(i);
            Listing listing = new Listing(
                    jsonObject.getString("title"),
                    jsonObject.getString("descr"),
                    jsonObject.getString("username"),
                    jsonObject.getInt("status"),
                    jsonObject.getLong("listing_id"));
            listings.add(listing);
        }
        return listings;
    }
    // helper method to call this from any controller.
    public static void GetListingsByUsername(Context context, ResultReceiver receiver, String owner){
        Intent i = new Intent(context, GetListingsService.class);
        HashMap<String,String> data = new HashMap<>();
        data.put("username", owner);
        i.putExtra("data",data);
        i.putExtra("type", httprequest.GET_CODE);
        i.putExtra("uri", httprequest.ROOT_ADDRESS + "/searchbyusername");
        i.putExtra("receiver", receiver);
        context.startService(i);
    }
    public static void GetListingsBySearch(Context context, ResultReceiver receiver, String query){
        Intent i = new Intent(context, GetListingsService.class);
        HashMap<String,String> data = new HashMap<>();
        data.put("like", query);
        i.putExtra("data",data);
        i.putExtra("type", httprequest.GET_CODE);
        i.putExtra("uri", httprequest.ROOT_ADDRESS + "/search");
        i.putExtra("receiver", receiver);
        context.startService(i);
    }
    public static void GetListingById(Context context, ResultReceiver receiver, Long listing_id){
        Intent i = new Intent(context, GetListingsService.class);
        HashMap<String,String> data = new HashMap<>();
        data.put("id", listing_id.toString());
        i.putExtra("data",data);
        i.putExtra("type", httprequest.GET_CODE);
        i.putExtra("uri", httprequest.ROOT_ADDRESS + "/searchbyID");
        i.putExtra("receiver", receiver);
        context.startService(i);
    }
}