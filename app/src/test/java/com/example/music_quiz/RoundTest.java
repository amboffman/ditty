package com.example.music_quiz;

import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoundTest {

    private Round roundClass;


@Mock Answers answersMock;
@Mock PlayerApi playerApiMock;
@Mock SpotifyAppRemote remoteMock;
@Mock AnswerShuffler answerShufflerMock;
@Mock PlayerState playerMock;

    @Before public void init(){roundClass = new Round(remoteMock, playerMock);}
    @Test
    public void roundConstructorTest(){
        Assert.assertEquals(remoteMock, roundClass.remote);
        Assert.assertEquals(playerMock, roundClass.player);
    }

    @Test
    public void setupTest() throws ExecutionException, InterruptedException {
        ArrayList answers = new ArrayList();
        roundClass.answers = answersMock;
        roundClass.player = playerMock;
        answers.add("a");
        answers.add("b");
        answers.add("c");
        answers.add("d");
        doNothing().when(answersMock).addAnswers(anyString());
        when(answersMock.fetchAnswers()).thenReturn(answers);
        ArrayList result = roundClass.setup();
        verify(answersMock, times(1)).clearAnswers();
        verify(answersMock, times(4)).addAnswers(anyString());
        verify(answersMock, times(1)).fetchAnswers();
        verify(answerShufflerMock).shuffle(any());
        Assert.assertEquals(answers, result);

    }

}
