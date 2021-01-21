package com.example.music_quiz;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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


    private void fetchPlaylists(){
        mSpotifyAppRemote.getContentApi().getRecommendedContentItems("DEFAULT")
                .setResultCallback(playlistRecommendations -> {
                    for(int i=0; i < 4; i++){
                        if (i == 0) {
                            playlist0.setText(playlistRecommendations.items[i].title);
                        }
                        else if (i == 1) {
                            playlist1.setText(playlistRecommendations.items[i].title);
                        }
                        else if (i == 2) {
                            playlist2.setText(playlistRecommendations.items[i].title);
                        }
                        else if (i == 3) {
                            playlist3.setText(playlistRecommendations.items[i].title);
                        }
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSpotifyAppRemote.getPlayerApi().pause();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        mSpotifyAppRemote.getPlayerApi().pause();
    }

}
