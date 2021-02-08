package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.music_quiz.START";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button actionButton = (Button) findViewById(R.id.actionButton);
        actionButton.setVisibility(View.GONE);
    }

    private void startGame() {
        // Do something in response to button
        Intent playlistSelectionActivity = new Intent(this, PlaylistSelectionActivity.class);
        playlistSelectionActivity.putExtra(EXTRA_MESSAGE, "start");
        startActivity(playlistSelectionActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView info= (TextView) findViewById(R.id.info);
        info.setText("");
        Connection connection = new Connection();
        connection.connectSpotify(this, new ConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d("Startup Connection:", "Successful");
                qualifyPlayer();
                connection.spotifyRemote.disconnect(connection.spotifyRemote);
            }

            @Override
            public void onError(String err) {
                Log.e("Startup Connection:", "Failed");
                if(err.equals("logged out connection err")){
                    //Spotify login
                    spotifyLogin();
                }
                else if(err.equals("no spotify connection err")){
                    downloadSpotify();
                }
                else if(err.equals("capabilities err")){
                    //Premium spotify needed
                }
            }
        });

    }
    private void downloadSpotify(){
        String url;
        try {
            //Check whether Google Play store is installed or not:
            this.getPackageManager().getPackageInfo("com.android.vending", 0);

            url = "market://details?id=" + "com.spotify.music";
        } catch ( final Exception e ) {
            url = "https://play.google.com/store/apps/details?id=" + "com.spotify.music";
        }

        final Intent appstoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        TextView errorInfo = (TextView) findViewById(R.id.info);
        errorInfo.setText("Please download the Spotify app to play");
        Button downloadSpotifyButton = (Button) findViewById(R.id.actionButton);
        downloadSpotifyButton.setBackgroundColor(Color.parseColor("#31a744"));
        downloadSpotifyButton.setText("Download Spotify");
        downloadSpotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appstoreIntent != null) {
                    startActivity(appstoreIntent);
                }
            }
        });
        downloadSpotifyButton.setVisibility(View.VISIBLE);
    }
    private void qualifyPlayer(){
        Button startGameButton = (Button) findViewById(R.id.actionButton);
        startGameButton.setBackgroundColor(Color.parseColor("#31a744"));
        startGameButton.setText("Start Game");
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        startGameButton.setVisibility(View.VISIBLE);
    }

    private void spotifyLogin(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.spotify.music");
        TextView errorInfo = (TextView) findViewById(R.id.info);
        errorInfo.setText("Please log into the Spotify app to play");
        Button spotifyLoginButton = (Button) findViewById(R.id.actionButton);
        spotifyLoginButton.setBackgroundColor(Color.parseColor("#31a744"));
        spotifyLoginButton.setText("Spotify Login");
        spotifyLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
        spotifyLoginButton.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}