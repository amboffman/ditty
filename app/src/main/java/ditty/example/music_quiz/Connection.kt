package ditty.example.music_quiz

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.NotLoggedInException
import com.spotify.android.appremote.api.error.SpotifyConnectionTerminatedException
import com.spotify.protocol.types.Capabilities

class Connection {
    @VisibleForTesting
    protected val CLIENT_ID = "afca0c6d0ea04e77b84465e4c5d9f2f3"
    protected val REDIRECT_URI = "app://music.quiz"
    @JvmField
    var spotifyRemote: SpotifyAppRemote? = null
    fun connectSpotify(context: Context?, cb: ConnectionCallback) {
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()
        SpotifyAppRemote.connect(context, connectionParams, object : ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                spotifyRemote = spotifyAppRemote
                Log.d("Connection", "Connected")
                // Now you can start interacting with App Remote
                checkSpotifyConditions(cb)
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                if (throwable is NotLoggedInException) {
                    cb.onError("logged out connection err")
                } else if (throwable is CouldNotFindSpotifyApp) {
                    cb.onError("no spotify connection err")
                } else if (throwable is SpotifyConnectionTerminatedException) {
                    cb.onError("terminated connection err")
                }
            }
        })
    }

    private fun checkSpotifyConditions(cb: ConnectionCallback) {
        spotifyRemote!!.userApi
                .capabilities
                .setResultCallback { capabilities: Capabilities ->
                    if (capabilities.canPlayOnDemand) {
                        cb.onSuccess()
                    } else {
                        cb.onError("capabilities err")
                    }
                }
    }
}