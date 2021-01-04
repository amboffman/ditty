package com.example.music_quiz;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Round (SpotifyAppRemote remote, PlayerState player) {
    Answers answers = new Answers();
    ArrayList fetchedAnswers = answers.fetchAnswers();
    public ArrayList setup() throws Exception {
        answers.clearAnswers();
       for(int i =0; i<4; i++){
            remote.getPlayerApi().skipNext()
                    .setResultsCallback(cb ->{
                        remote.getPlayerApi().pause();
                        answers.addAnswers(player.track.title);
                        int answersSize = answers.fetchAnswers().size();
                        if(answersSize > 3){
                            Long startMs = nextLong(new Random(player.track.duration),((player.track.duration - 30000)));
                            remote.getPlayerApi().seekToRelativePosition(startMs);
                            fetchedAnswers = answers.fetchAnswers();
                            return fetchedAnswers;
                        }
                    });
        }
    return fetchedAnswers;
    }
    public void play() throws InterruptedException {
        remote.getPlayerApi().resume();
        TimeUnit.SECONDS.sleep(15);
        remote.getPlayerApi().pause();
    }

    public boolean next(String title) throws Exception {
        if(title.equals(player.track.title)){
            return true;
        }
        else{
            return false;
        }
    }

    private long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }

}
