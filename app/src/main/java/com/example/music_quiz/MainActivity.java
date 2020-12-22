package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;


public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "afca0c6d0ea04e77b84465e4c5d9f2f3";
    private static final String REDIRECT_URI = "app://music.quiz";
    private SpotifyAppRemote mSpotifyAppRemote;
    private Track song;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Set the connection parameters
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener(){
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote){
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected! Yay!");
                // Now you can start interacting with App Remote
                connected();
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);

                // Something went wrong when attempting to connect! Handle errors here
            }
        });
    }

    private void connected() {

        //Play on phone
//        mSpotifyAppRemote.getConnectApi().connectSwitchToLocalDevice();
        //Shuffle playlist
        mSpotifyAppRemote.getPlayerApi().setShuffle(true);
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState()
                .setEventCallback(playerState->{
             final Track track = playerState.track;
            if(track!=null){
                this.song = track;
            }
        });
        // Play
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:4MKj7uOwJYPuj1zJXL3qBr");

        //Update answer button to song title
        Button answer1_button = (Button)findViewById(R.id.answer1);
        answer1_button.setText(song.name);


    }

    @Override
    protected void onStop() {
        super.onStop();
            SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        }

}