package com.example.music_quiz;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnswerTest {

    @Mock
    Answers answerService;

    @Test
    public void testSetAnswer(){
    answerService.setAnswer(anyString());
    verify(answerService, times(1)).setAnswer(anyString());
    }

    @Test
    public void testFetchAnswer() throws Exception {
        when(answerService.fetchAnswer()).thenReturn(answerService.answers);
        answerService.fetchAnswer();
        verify(answerService, times(1)).fetchAnswer();
    Assert.assertEquals("Test Title", answerService.fetchAnswer());
    }

    @Test (expected = Exception.class)
    public void testFetchAnswerNull() throws Exception{
        answerService.answers = null;
        doThrow(Exception.class).when(!(answerService.fetchAnswer()).equals(notNull()));
        answerService.fetchAnswer();
        verify(answerService, times(1)).fetchAnswer();
    }

    @Test
    public void testClearAnswer(){
        answerService.clearAnswer();
        verify(answerService, times(1)).clearAnswer();
    }

    @Test
    public void testVerifyAnswer(){
        when(answerService.verifyAnswer(anyString())).thenReturn(true);
        Assert.assertEquals(true, answerService.verifyAnswer("Test Title"));
    }

}