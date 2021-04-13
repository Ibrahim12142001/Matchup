package com.example.multipart2;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class Treasure extends AppCompatActivity {
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treasure);

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity3();
            }
        });


    }
    public void openMainActivity3(){

        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }


}
