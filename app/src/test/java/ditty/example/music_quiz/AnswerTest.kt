package ditty.example.music_quiz

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class AnswerTest {
    var answersClass = Answers()

    @Test
    fun testAddAnswers() {
//        List of 4 or less stores in order
        var expected: MutableList<String> = Arrays.asList("a", "b", "c", "d")
        answersClass.addAnswers("a")
        answersClass.addAnswers("b")
        answersClass.addAnswers("c")
        answersClass.addAnswers("d")
        val stored = answersClass.answers
        Assert.assertEquals("Add < 5 answers", expected, stored)
        expected = Arrays.asList("e", "b", "c", "d")
        answersClass.addAnswers("e")
        Assert.assertEquals("Add > 5 answers", expected, stored)
    }

    @Test
    fun testFetchAnswer() {
        var answers: ArrayList<String> = ArrayList<String>()
        answers.add("a")
        answersClass.answers = answers;
        val result = answersClass.fetchAnswers()
        Assert.assertEquals(answers, result)
    }

    @Test
    fun testClearAnswer() {
        val addition: ArrayList<String> = ArrayList<String>()
        addition.add("a")
        answersClass.answers = addition
        answersClass.clearAnswers()
        val expected: ArrayList<*> = ArrayList<Any?>()
        Assert.assertEquals(expected, answersClass.answers)
    }

    @Test
    fun testVerifyAnswer() {
        val answers: ArrayList<String> = ArrayList<String>()
        answers.add("a")
        answers.add("b")
        answersClass.answers = answers
        var result = answersClass.verifyAnswer("a", 0)
        Assert.assertEquals("Verify correct answer", true, result)
        result = answersClass.verifyAnswer("a", 1)
        Assert.assertEquals("Verify incorrect answer", false, result)
    }
}