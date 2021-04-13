package com.example.multipart2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Game extends AppCompatActivity {
    MediaPlayer mySong;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabs, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Intent intent1 = new Intent(this, AboutUs.class);
                this.startActivity(intent1);
                return true;
            case R.id.soon:
                Intent intent2 = new Intent(this, soon.class);
                this.startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mySong = MediaPlayer.create(Game.this, R.raw.backsound);

        final MediaPlayer startSound = MediaPlayer.create(this, R.raw.startsound);
        final Button START_GAME = (Button) this.findViewById(R.id.button3);
        final Button START_GAME2 = (Button) this.findViewById(R.id.button4);
        START_GAME.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startSound.start();
                mySong.start();
                startActivity(new Intent(Game.this, Game2.class));
            }
        });

        START_GAME2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startSound.start();
                mySong.start();
                startActivity(new Intent(Game.this, Game3.class));
            }
        });


    }
}