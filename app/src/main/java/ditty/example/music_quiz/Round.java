package ditty.example.music_quiz;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Round implements AnswerShuffler {

    SpotifyAppRemote remote;
    PlayerState player;
    AnswerShuffler answerShuffler;
    public Round (SpotifyAppRemote passedRemote, PlayerState passedPlayer){
        this.remote = passedRemote;
        this.player = passedPlayer;
    }

    public ArrayList<Answers> shuffle(ArrayList <Answers> answers){
        Collections.shuffle(answers);
    return answers;
    }

    Answers answers = new Answers();
    private void setTracks(){
        for(int i =0; i<4; i++){
            remote.getPlayerApi().skipNext()
                    .setResultCallback(cb ->{
                        answers.addAnswers(player.track.name);
                        if(answers.fetchAnswers().size() > 3){
                            Long startMs = nextLong(new Random(player.track.duration),((player.track.duration - 30000)));
                            remote.getPlayerApi().seekToRelativePosition(startMs);

                        }
                    });
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList setup() throws ExecutionException, InterruptedException {
        answers.clearAnswers();
       CompletableFuture<Void> setFuture = CompletableFuture.supplyAsync(()->{
          setTracks();
          return null;
       });
        CompletableFuture<ArrayList> fetchFuture = setFuture.thenApply(s->{
        ArrayList<Answers> fetched = answers.fetchAnswers();
        shuffle(fetched);
        return fetched;
        });

    return fetchFuture.get();
    }

    public void play() throws InterruptedException {
        remote.getPlayerApi().resume();
        TimeUnit.SECONDS.sleep(15);
        remote.getPlayerApi().pause();
    }


    public boolean next(String title) throws Exception {
        if(title.equals(player.track.name)){
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
