package com.example.lendahand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class createServiceOpDateTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service_op_date_time);
        //STEP 1: Add reference to button using R.id
        MaterialButton createServiceOpCatReq = findViewById(R.id.btnServiceOpDateTimeNext);

        //STEP 2: Set onClickListener for YOUR button
        createServiceOpCatReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //STEP 3: Create Intent for your class
                Intent createServiceOpScreen = new Intent(v.getContext(), createServiceOpCatReq.class);
                //STEP 4: Start your Activity
                startActivity(createServiceOpScreen);
            }
        });
    }
}