package com.example.multipart2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Vibrator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class MainActivity2 extends AppCompatActivity {
    ListView listView;
    Button button;
    Vibrator vibrator;
    List<String> roomsList;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        database = FirebaseDatabase.getInstance();

        // get the player name and assign his room name to the player name
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        roomName = playerName;

        listView = findViewById(R.id.ListView);
        button = findViewById(R.id.button);

        //all existing available rooms
        roomsList = new ArrayList<>();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create room and add yourself as player1
                vibrator.vibrate(200);
                button.setText("CREATING ROOM");
                button.setEnabled(false);
                roomName = playerName;
                roomRef=database.getReference("rooms/"+roomName+"/"+"players/"+playerName);
                addRoomEventListener();
                roomRef.setValue("");
                roomRef = database.getReference("rooms/" + roomName + "/Lobby ID");
                roomRef.setValue(getAlphaNumericStringoflength32());
                roomRef = database.getReference("rooms/" + roomName + "/Number of people");
                roomRef.setValue(1);



            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //join an existing room and add yourself as player2
                roomName = roomsList.get(position);
                roomRef=database.getReference("rooms/"+roomName+"/"+"players/"+playerName);
                roomRef.setValue("");
                roomRef = database.getReference("rooms/" + roomName + "/Number of people");
                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int n = dataSnapshot.getValue(int.class);
                        database.getReference("rooms/"+roomName+"/Number of people").setValue(n+1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                addRoomEventListener();

            }
        });

        //show if new room is available
        addRoomsEventListener();
    }


    private void addRoomEventListener() {
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // join the room
                button.setText("CREATE ROOM");
                button.setEnabled(true);

                Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error
                button.setText("CREATE ROOM");
                button.setEnabled(true);
                Toast.makeText(MainActivity2.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventListener() {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //show list of rooms
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : rooms) {
                    roomsList.add(snapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this,
                            android.R.layout.simple_list_item_1, roomsList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error - nothing
            }
        });
    }

    // function to generate a random string of length n
    static String getAlphaNumericStringoflength32() {
        int n = 32;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}