package com.example.music_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.ListItem;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "afca0c6d0ea04e77b84465e4c5d9f2f3";
    private static final String REDIRECT_URI = "app://music.quiz";
    private SpotifyAppRemote mSpotifyAppRemote;
    private String playlistUri;
    private ArrayList<String> answers = new ArrayList<String>();

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

        mSpotifyAppRemote.getContentApi().getRecommendedContentItems("DEFAULT")
                .setResultCallback(playlistRecommendations -> {
                    final ListItem recentlyPlayedPlaylists = playlistRecommendations.items[0];
                    mSpotifyAppRemote.getContentApi().getChildrenOfItem(recentlyPlayedPlaylists, 5, 0)
                        .setResultCallback(playlists->{
                            final ListItem lastPlaylist = playlists.items[0];
                            this.playlistUri = lastPlaylist.uri;
                        });
        });
        //Play on phone
        mSpotifyAppRemote.getConnectApi().connectSwitchToLocalDevice();
        //Shuffle playlist
        mSpotifyAppRemote.getPlayerApi().setShuffle(true);
        // Play
        mSpotifyAppRemote.getPlayerApi().play(this.playlistUri);

//        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerContext();
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if(!answers.contains(playerState.track.name)) {
                        answers.add(playerState.track.name);
                        if(answers.size() < 4){
                            mSpotifyAppRemote.getPlayerApi().skipNext();
                        }
                    }
                    if(answers.size() ==4) {
                        setAnswers();
                    }
                });


    }

    private void setAnswers(){
        Collections.shuffle(answers);
        Log.d("Answers", String.valueOf(answers));
        //Update answer button to song title
        Button answer0_button = (Button)findViewById(R.id.answer0);
        answer0_button.setText(String.valueOf(this.answers.get(0)));

        Button answer1_button = (Button)findViewById(R.id.answer1);
        answer1_button.setText(String.valueOf(this.answers.get(1)));

        Button answer2_button = (Button)findViewById(R.id.answer2);
        answer2_button.setText(String.valueOf(this.answers.get(2)));

        Button answer3_button = (Button)findViewById(R.id.answer3);
        answer3_button.setText(String.valueOf(this.answers.get(3)));
    }
    @Override
    protected void onStop() {
        super.onStop();
        mSpotifyAppRemote.getPlayerApi().pause();
            SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        }

}