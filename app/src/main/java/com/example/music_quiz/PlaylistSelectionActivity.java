package com.example.music_quiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ListItem;

import java.util.ArrayList;

public class PlaylistSelectionActivity extends AppCompatActivity {
    private SpotifyAppRemote mSpotifyAppRemote;
    private ListItem[] playlists;
    public ArrayList playlistUris = new ArrayList();
    private ArrayList playlistTitles = new ArrayList();
    private  ArrayList playlistImages = new ArrayList();
    public static final String EXTRA_PLAYLIST_URI = "com.example.music_quiz.PLAYLISTURI";

    Button playlist0;
    Button playlist1;
    Button playlist2;
    Button playlist3;

    Connection connection = new Connection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_selection);

    }

    @Override
    protected void onStart() {
        super.onStart();
        playlist0 = (Button) findViewById(R.id.playlist0);
        playlist1 = (Button) findViewById(R.id.playlist1);
        playlist2 = (Button) findViewById(R.id.playlist2);
        playlist3 = (Button) findViewById(R.id.playlist3);
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Set the connection parameters
        connection.connectSpotify(this, new ConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d("Quiz Connection:", "Successful");
                mSpotifyAppRemote = connection.getSpotifyRemote();
                fetchPlaylists();
            }

            @Override
            public void onError(String err) {
                Log.e("Quiz Connection:", "Failed");
                if(err.equals("connection err")){
                    //Spotify login
                }
                else if(err.equals("capabilities err")){
                    //Premium spotify needed
                }
            }
        });


    }

    public void playPlaylist (String playlistURI){
            // Do something in response to button
        Log.d("PLAYING", String.valueOf(playlistURI));
            Intent gameActivity = new Intent(this, GameActivity.class);
            gameActivity.putExtra(EXTRA_PLAYLIST_URI, playlistURI);
            startActivity(gameActivity);
    };



    private void fetchPlaylists(){
        mSpotifyAppRemote.getContentApi().getRecommendedContentItems("DEFAULT")
                .setResultCallback(playlistRecommendations -> {
                    mSpotifyAppRemote.getContentApi().getChildrenOfItem(playlistRecommendations.items[0], 4, 0)
                            .setResultCallback(
                                    recentlyPlayedPlaylists -> {
                                        for(int i=0; i < 4; i++){
                                            playlistUris.add(recentlyPlayedPlaylists.items[i].uri);
                                            if (i == 0) {
                                                playlist0.setText(recentlyPlayedPlaylists.items[i].title);
                                                playlist0.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        playPlaylist(recentlyPlayedPlaylists.items[0].uri);
                                                            }
                                                    });
                                            }
                                            else if (i == 1) {
                                                playlist1.setText(recentlyPlayedPlaylists.items[i].title);
                                                playlist1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        playPlaylist(recentlyPlayedPlaylists.items[1].uri);
                                                    }
                                                });
                                            }
                                            else if (i == 2) {
                                                playlist2.setText(recentlyPlayedPlaylists.items[i].title);
                                                playlist2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        playPlaylist(recentlyPlayedPlaylists.items[2].uri);
                                                    }
                                                });

                                            }
                                            else if (i == 3) {
                                                playlist3.setText(recentlyPlayedPlaylists.items[i].title);
                                                playlist3.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        playPlaylist(recentlyPlayedPlaylists.items[3].uri);
                                                    }
                                                });
                                            }
                                        }
                                    }
                            );
                });

    }
    @Override
    protected void onPause() {
        super.onPause();
        mSpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

}
