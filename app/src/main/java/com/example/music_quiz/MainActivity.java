package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.music_quiz.START";
    public static final String EXTRA_MESSAGE_MODE = "com.example.music_quiz.MODE";
    public boolean endlessMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ConstraintLayout layout = findViewById(R.id.main_layout);
        layout.setBackgroundResource(R.drawable.gradient_animation);
        AnimationDrawable animDrawable = (AnimationDrawable) layout.getBackground();
        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();
        Button actionButton = (Button) findViewById(R.id.actionButton0);
        actionButton.setVisibility(View.GONE);
        Button actionButton2 = (Button) findViewById(R.id.actionButton1);
        actionButton2.setVisibility(View.GONE);
    }

    private void startGame() {
        // Do something in response to button
        Intent playlistSelectionActivity = new Intent(this, PlaylistSelectionActivity.class);
        playlistSelectionActivity.putExtra(EXTRA_MESSAGE, "start");
        playlistSelectionActivity.putExtra(EXTRA_MESSAGE_MODE, endlessMode);
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
upgradeToPremium();
                }
            }
        });

    }

    private void upgradeToPremium(){
        String url = "https://www.spotify.com/us/premium/";

        final Intent premiumIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        TextView errorInfo = (TextView) findViewById(R.id.info);
        errorInfo.setText("Spotify Premium is required for this application.");
        Button downloadSpotifyButton = (Button) findViewById(R.id.actionButton0);
        downloadSpotifyButton.setBackgroundResource(R.color.emerald);
        downloadSpotifyButton.setText("Upgrade to Spotify Premium");
        downloadSpotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (premiumIntent != null) {
                    startActivity(premiumIntent);
                }
            }
        });
        downloadSpotifyButton.setVisibility(View.VISIBLE);
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
        errorInfo.setText("The Spotify app is required for this application.");
        Button downloadSpotifyButton = (Button) findViewById(R.id.actionButton0);
        downloadSpotifyButton.setBackgroundResource(R.color.emerald);
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
        Button challengeModeButton = (Button) findViewById(R.id.actionButton0);
        challengeModeButton.setBackgroundResource(R.color.emerald);
        challengeModeButton.setText("Challenge Mode");
        challengeModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endlessMode = false;
                startGame();
            }
        });
        Button endlessModeButton = (Button) findViewById(R.id.actionButton1);
        endlessModeButton.setBackgroundResource(R.color.emerald);
        endlessModeButton.setText("Endless Mode");
        endlessModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endlessMode = true;
                startGame();
            }
        });
        challengeModeButton.setVisibility(View.VISIBLE);
        endlessModeButton.setVisibility(View.VISIBLE);
    }

    private void spotifyLogin(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.spotify.music");
        TextView errorInfo = (TextView) findViewById(R.id.info);
        errorInfo.setText("Please log into the Spotify app.");
        Button spotifyLoginButton = (Button) findViewById(R.id.actionButton0);
        spotifyLoginButton.setBackgroundResource(R.color.emerald);
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