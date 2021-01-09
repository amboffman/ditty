package com.example.music_quiz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AnswerTest {

Answers answersClass = new Answers();

    @Test
    public void testAddAnswers(){
//        List of 4 or less stores in order
        List testAnswers = Arrays.asList("a", "b", "c", "d");
    answersClass.addAnswers("a");
    answersClass.addAnswers("b");
    answersClass.addAnswers("c");
    answersClass.addAnswers("d");
    ArrayList stored = answersClass.answers;
    ArrayList expected = new ArrayList();
    expected.addAll(testAnswers);
    assertEquals("Add < 5 answers", expected, stored);

    testAnswers = Arrays.asList("e", "b", "c", "d");
    expected.clear();
    expected.addAll(testAnswers);
    answersClass.addAnswers("e");
    assertEquals("Add > 5 answers",expected, stored);
    }

    @Test
    public void testFetchAnswer(){
        ArrayList answers = new ArrayList();
        answers.add("a");
        answersClass.answers = answers;
        ArrayList result = answersClass.fetchAnswers();
        assertEquals(answers, result);
    }

    @Test
    public void testClearAnswer(){
        ArrayList addition = new ArrayList();
        addition.add("a");
        answersClass.answers = addition;
        answersClass.clearAnswers();
        ArrayList expected = new ArrayList();
        assertEquals(expected, answersClass.answers);
    }

    @Test
    public void testVerifyAnswer(){
        ArrayList answers = new ArrayList();
        answers.add("a");
        answers.add("b");
        answersClass.answers = answers;
        Boolean result = answersClass.verifyAnswer("a", 0);
        assertEquals("Verify correct answer",true, result);
        result = answersClass.verifyAnswer("a", 1);
        assertEquals("Verify incorrect answer",false, result);

    }

}