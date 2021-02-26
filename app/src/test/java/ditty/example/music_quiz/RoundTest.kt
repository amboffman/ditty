package ditty.example.music_quiz

import com.spotify.android.appremote.api.PlayerApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import java.util.concurrent.ExecutionException

@RunWith(MockitoJUnitRunner::class)
class RoundTest {
    private var roundClass: Round? = null

    @Mock
    var answersMock: Answers? = null

    @Mock
    var playerApiMock: PlayerApi? = null

    @Mock
    var remoteMock: SpotifyAppRemote? = null

    @Mock
    var answerShufflerMock: AnswerShuffler? = null

    @Mock
    var playerMock: PlayerState? = null

    @Before
    fun init() {
        roundClass = Round(remoteMock, playerMock)
    }

    @Test
    fun roundConstructorTest() {
        Assert.assertEquals(remoteMock, roundClass!!.remote)
        Assert.assertEquals(playerMock, roundClass!!.player)
    }

    @Test
    @Throws(ExecutionException::class, InterruptedException::class)
    fun setupTest() {
        val answers: ArrayList<String> = ArrayList<String>()
        roundClass!!.answers = answersMock
        roundClass!!.player = playerMock
        answers.add("a")
        answers.add("b")
        answers.add("c")
        answers.add("d")
        Mockito.doNothing().`when`(answersMock)!!.addAnswers(ArgumentMatchers.anyString())
        Mockito.`when`(answersMock!!.fetchAnswers()).thenReturn(answers)
        val result = roundClass!!.setup()
        Mockito.verify(answersMock, Mockito.times(1))!!.clearAnswers()
        Mockito.verify(answersMock, Mockito.times(4))!!.addAnswers(ArgumentMatchers.anyString())
        Mockito.verify(answersMock, Mockito.times(1))!!.fetchAnswers()
        Mockito.verify(answerShufflerMock)!!.shuffle(ArgumentMatchers.any())
        Assert.assertEquals(answers, result)
    }
}