package com.example.multipart2;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {

    Button button2;
    Vibrator vibrator;
    String playerName = "";
    String roomName = "";
    FirebaseDatabase database;
    DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        button2 = findViewById(R.id.button2);
        database = FirebaseDatabase.getInstance();
        //get the player name and assign his room name to the player name
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomName = extras.getString("roomName");
        }
        if (extras != null) {
            roomName = extras.getString("roomName");
            if (roomName.equals(playerName)) {
                final Button button;
                button = findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vibrator.vibrate(200);
                        button.setEnabled(false);
                        roomRef = database.getReference("rooms/" + roomName + "/Number of people");
                        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int n = dataSnapshot.getValue(int.class);
                                if (n >= 2) {

                                    Game();

                                }
                                else{
                                    Toast.makeText(MainActivity3.this, "Cannot start with only one player", Toast.LENGTH_SHORT).show();
                                    button.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
            else {
                final Button button;
                button = findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vibrator.vibrate(200);
                        button.setEnabled(false);
                        // Toast.makeText(MainActivity3.this, "You are not the host!", Toast.LENGTH_SHORT).show();
                        // TODO: Update below to start intent for all clients
                        Intent passdata_intent = new Intent(v.getContext(), Game.class);
                        passdata_intent.putExtra("playerName", playerName);
                        startActivity(passdata_intent);
                        Intent intent = new Intent(getApplicationContext(), Game.class);
                        startActivity(intent);
                    }
                });
            }


            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(200);
                    button2.setEnabled(false);
                    button2.setText("Exiting room");
                    roomRef = database.getReference("rooms/" + roomName + "/Number of people");
                    roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int n = dataSnapshot.getValue(int.class);
                            if (roomName.equals(playerName)) {
                                database.getReference("rooms/" + roomName).removeValue();
                                //if the host is exiting,delete the whole room
                            }
                            else {
                                database.getReference("rooms/" + roomName + "/Number of people").setValue(n - 1);
                                database.getReference("rooms/" + roomName + "/players/" + playerName).removeValue();
                                //delete the member
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });


        }

    }
    @Override
    //when the back button is pressed,do the same as the exit room button is pressed
    public void onBackPressed() {
        roomRef = database.getReference("rooms/" + roomName + "/Number of people");
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int n = dataSnapshot.getValue(int.class);
                if (roomName.equals(playerName)) {
                    database.getReference("rooms/" + roomName).removeValue();
                    //if the host is exiting,delete the whole room
                }
                else {
                    database.getReference("rooms/" + roomName + "/Number of people").setValue(n - 1);
                    database.getReference("rooms/" + roomName + "/players/" + playerName).removeValue();
                    //delete the member
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Game(){
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

}