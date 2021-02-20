package com.example.music_quiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ListItem;
import com.spotify.protocol.types.ListItems;

import java.util.ArrayList;

public class PlaylistSelectionActivity extends AppCompatActivity {
    private SpotifyAppRemote mSpotifyAppRemote;
    private ListItems playlists;
    public ArrayList playlistUris = new ArrayList();
    private ArrayList playlistTitles = new ArrayList();
    private  ArrayList<Bitmap> playlistImages = new ArrayList();
    public static final String EXTRA_PLAYLIST_URI = "com.example.music_quiz.PLAYLISTURI";
    public static final String EXTRA_MODE= "com.example.music_quiz.MODE";
    private boolean endlessMode;
    GridView playlistGrid;
    Button playlist0;
    Button playlist1;
    Button playlist2;
    Button playlist3;

    Connection connection = new Connection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_selection);
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ConstraintLayout layout = findViewById(R.id.playlistSelectionScreen);
        layout.setBackgroundResource(R.drawable.gradient_animation);
        AnimationDrawable animDrawable = (AnimationDrawable) layout.getBackground();
        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();
        playlistGrid = findViewById(R.id.playlists);


        endlessMode = getIntent().getExtras().getBoolean(MainActivity.EXTRA_MESSAGE_MODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        playlist0 = (Button) findViewById(R.id.playlist0);
        playlist1 = (Button) findViewById(R.id.playlist1);
        playlist2 = (Button) findViewById(R.id.playlist2);
        playlist3 = (Button) findViewById(R.id.playlist3);
        playlist0.setVisibility(View.GONE);
        playlist1.setVisibility(View.GONE);
        playlist2.setVisibility(View.GONE);
        playlist3.setVisibility(View.GONE);
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
                returnToHomeScreen();
            }
        });


    }
    private void returnToHomeScreen(){
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }
    public void playPlaylist (String playlistURI){
            // Do something in response to button
        Log.d("PLAYING", String.valueOf(playlistURI));
            Intent gameActivity = new Intent(this, GameActivity.class);
            gameActivity.putExtra(EXTRA_PLAYLIST_URI, playlistURI);
            gameActivity.putExtra(EXTRA_MODE, endlessMode);
            startActivity(gameActivity);
    };



    private void fetchPlaylists(){
        mSpotifyAppRemote.getContentApi().getRecommendedContentItems("DEFAULT")
                .setResultCallback(playlistRecommendations -> {
                    mSpotifyAppRemote.getContentApi().getChildrenOfItem(playlistRecommendations.items[0], 20, 0)
                            .setResultCallback(
                                    recentlyPlayedPlaylists -> {
//                                        for(int i=0; i < 4; i++){
//                                            playlistUris.add(recentlyPlayedPlaylists.items[i].uri);
//                                            if (i == 0) {
//                                                playlist0.setText(recentlyPlayedPlaylists.items[i].title);
//                                                playlist0.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        playPlaylist(recentlyPlayedPlaylists.items[0].uri);
//                                                            }
//                                                    });
//                                                playlist0.setVisibility(View.VISIBLE);
//                                            }
//                                            else if (i == 1) {
//                                                playlist1.setText(recentlyPlayedPlaylists.items[i].title);
//                                                playlist1.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        playPlaylist(recentlyPlayedPlaylists.items[1].uri);
//                                                    }
//                                                });
//                                                playlist1.setVisibility(View.VISIBLE);
//                                            }
//                                            else if (i == 2) {
//                                                playlist2.setText(recentlyPlayedPlaylists.items[i].title);
//                                                playlist2.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        playPlaylist(recentlyPlayedPlaylists.items[2].uri);
//                                                    }
//                                                });
//                                                playlist2.setVisibility(View.VISIBLE);
//                                            }
//                                            else if (i == 3) {
//                                                playlist3.setText(recentlyPlayedPlaylists.items[i].title);
//                                                playlist3.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        playPlaylist(recentlyPlayedPlaylists.items[3].uri);
//                                                    }
//                                                });
//                                                playlist3.setVisibility(View.VISIBLE);
//                                            }
//                                        }
                                        for(int i=0; i < recentlyPlayedPlaylists.total; i++){
                                            playlists = recentlyPlayedPlaylists;
                                            mSpotifyAppRemote.getImagesApi().getImage(recentlyPlayedPlaylists.items[i].imageUri)
                                                    .setResultCallback(image->{
                                                        playlistImages.add(image);
                                                        if(playlistImages.size() == (recentlyPlayedPlaylists.total -1) ){
                                                PlaylistAdapter playlistAdapter = new PlaylistAdapter(PlaylistSelectionActivity.this, R.layout.row_item, playlists, playlistImages);
                                                playlistGrid.setAdapter(playlistAdapter);
                                                playlistGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                     playPlaylist((String) playlistUris.get(position));
                                                    }
                                                });
                                                        }
                                                    });
//                                            if(i == (recentlyPlayedPlaylists.total - 1)){
//                                            }
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
