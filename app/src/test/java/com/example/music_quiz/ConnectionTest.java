package com.example.music_quiz;

import android.content.Context;
import android.telecom.Call;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.UserApi;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Capabilities;
import com.spotify.protocol.types.Types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.security.auth.callback.Callback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionTest {

@Mock
ConnectionCallback mConnectionCallback;
@Mock
Context mContext;
@Mock
SpotifyAppRemote mSpotifyAppRemote;

Connection tConnection = new Connection();

@Test
    public void connectionSuccess(){
    tConnection.connectSpotify(mContext, mConnectionCallback);



}

}
