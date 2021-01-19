package com.example.music_quiz;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.error.UnsupportedFeatureVersionException;

public class Connection {
    @VisibleForTesting
    protected final String CLIENT_ID = "afca0c6d0ea04e77b84465e4c5d9f2f3";
    protected final String REDIRECT_URI = "app://music.quiz";

    public SpotifyAppRemote spotifyRemote;

    public void connectSpotify(Context context, ConnectionCallback cb){

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(context, connectionParams, new Connector.ConnectionListener(){
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                spotifyRemote = spotifyAppRemote;
                Log.d("Connection", "Connected! Yay!");
                // Now you can start interacting with App Remote
                checkSpotifyConditions(cb);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            cb.onError("connection err");
                // Something went wrong when attempting to connect! Handle errors here
            }
        });
    }

    SpotifyAppRemote getSpotifyRemote(){
       return spotifyRemote;
    };

    private void checkSpotifyConditions(ConnectionCallback cb){
        spotifyRemote.getUserApi()
                .getCapabilities()
                .setResultCallback(capabilities -> {
                    if(capabilities.canPlayOnDemand){
                        cb.onSuccess();
                    }
                    else{
                        cb.onError("capabilities err");
                    }
                });
    };

}
