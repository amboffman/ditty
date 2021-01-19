package com.example.music_quiz;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class Startup extends AppCompatActivity {
    Button start_game_button = (Button) findViewById(R.id.startGame);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_game_button = (Button) findViewById(R.id.startGame);
        start_game_button.setVisibility(View.GONE);
        start_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Connection connection = new Connection();
        connection.connectSpotify(this, new ConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d("Startup Connection:", "Successful");
                qualifyPlayer();
            }

            @Override
            public void onError(String err) {
                Log.e("Startup Connection:", "Failed");
            if(err.equals("connection err")){
                //Spotify login
            }
            else if(err.equals("capabilities err")){
                //Premium spotify needed
            }
            }
        });
    }

    private void qualifyPlayer(){
        start_game_button.setVisibility(View.VISIBLE);
    }
    private void startGame(){
        Intent gameActivity = new Intent(this, GameActivity.class);
        startActivity(gameActivity);
    };

}

