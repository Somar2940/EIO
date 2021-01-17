package com.example.eio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TYScreen extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tys);

        Button sendButton = findViewById(R.id.clearButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentstart = new Intent(getApplication(),MainActivity.class);
                startActivity(intentstart);
            }
        });
    }
}