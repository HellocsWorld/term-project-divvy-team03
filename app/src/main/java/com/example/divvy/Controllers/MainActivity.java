package com.example.divvy.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.divvy.GetListingsService;
import com.example.divvy.NetworkReceiver;
import com.example.divvy.R;

public class MainActivity extends AppCompatActivity implements NetworkReceiver.GetListingReceiver {

    public static String USERNAME;
    public NetworkReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mReceiver = new NetworkReceiver(null, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createListingButton = findViewById(R.id.create_listing_btn);
        createListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateListingActivity();
            }
        });
        Button displayListings = findViewById(R.id.display_listings_btn);
        displayListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("yes", "yes");
                GetListingsService.GetListingsByUsername(MainActivity.this,mReceiver);
            }
        });

        Button cancel = findViewById(R.id.register_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMessagingActivity();
            }
        });

    }
    private void startCreateListingActivity(){
        Intent i = new Intent(this, CreateListing.class);
        startActivity(i);
    }
    private void startDisplayListings(){
        Intent i = new Intent(this, DisplayListingsActivity.class);
        startActivity(i);
    }

    private void startMessagingActivity(){
        Intent i = new Intent(this, MessagingActivity.class);
        startActivity(i);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Intent i = new Intent(this, DisplayListingsActivity.class);
        i.putExtra("data", resultData.getSerializable("data"));
        startActivity(i);
    }
}
