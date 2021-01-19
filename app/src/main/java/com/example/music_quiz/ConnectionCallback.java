package com.example.music_quiz;

import com.spotify.protocol.types.Capabilities;

public interface ConnectionCallback {
    void onSuccess();
    void onError(String err);
}
