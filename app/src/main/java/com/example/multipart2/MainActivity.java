package com.example.multipart2;



import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// import com.google.firebase.auth.AuthResult;
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    Button button2;
    Vibrator vibrator;
    String playerName = "";
    MediaPlayer mySong;
    FirebaseDatabase database;
    DatabaseReference playerRef;

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
        setContentView(R.layout.activity_main);
        mySong = MediaPlayer.create(MainActivity.this, R.raw.backsound);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        database = FirebaseDatabase.getInstance();

        // Checks if player exists and get reference
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        if(playerName.equals("")){
            playerRef = database.getReference("players/" + playerName);
            addEventListener();
            playerRef.setValue("");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logging the player in
                vibrator.vibrate(200);
                playerName = editText.getText().toString();
                editText.setText("");
                if(!playerName.equals("")) {
                    button.setText("Logging in");
                    button.setEnabled(false);
                    playerRef = database.getReference("players/" + playerName);
                    addEventListener();
                    playerRef.setValue("");
                }
            }
        });

    }

    private void addEventListener() {
        //read from database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //success - continue to the next screen after saving the player name
                if(!playerName.equals("")) {
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error
                button.setText("Start Game");
                button.setEnabled(true);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}