package ditty.example.music_quiz

import android.content.Context
import com.spotify.android.appremote.api.SpotifyAppRemote
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConnectionTest {
    @Mock
    var mConnectionCallback: ConnectionCallback? = null

    @Mock
    var mContext: Context? = null

    @Mock
    var mSpotifyAppRemote: SpotifyAppRemote? = null
    var tConnection = Connection()

    @Test
    fun connectionSuccessCalls() {
        tConnection.connectSpotify(mContext, mConnectionCallback)
    }
}