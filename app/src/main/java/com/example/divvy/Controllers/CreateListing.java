package com.example.divvy.Controllers;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.divvy.Listing;
import com.example.divvy.R;

public class CreateListing extends AppCompatActivity {
    private final int REQUEST_CODE_CANCEL = 0;
    private final int REQUEST_CODE_SUBMIT = 1;

    Listing listing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);
        Button submit = findViewById(R.id.create_list_submit);
        Button cancel = findViewById(R.id.create_list_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //just go back
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make request to send data to backend
                EditText title = findViewById(R.id.create_list_name);
                EditText desc = findViewById(R.id.create_list_desc);
                EditText capacity = findViewById(R.id.create_grp_size);
                Log.d("capacity", capacity.toString());
                Integer size = Integer.parseInt(capacity.getText().toString());
                listing = new Listing(
                        title.getText().toString(),
                        desc.getText().toString(),
                        size,"owner",
                        "status",
                        01);
                listing.sendData();
                //if successful, go to next page
                //else keep user here
            }
        });
    }
}
