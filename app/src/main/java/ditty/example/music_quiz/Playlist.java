package ditty.example.music_quiz;

import android.graphics.Bitmap;

import com.spotify.protocol.types.Item;
import com.spotify.protocol.types.ListItems;

import java.util.ArrayList;

public class Playlist {
    private ListItems playlists;
    public ArrayList playlistUris = new ArrayList();
    private  ArrayList<Bitmap> playlistImages = new ArrayList();
    protected void setPlaylists(ListItems passedPlaylists){
        playlists = passedPlaylists;
    }
    protected ListItems getPlaylists(){
        return  playlists;
    }
    protected void addPlaylistUri(String playlistUri){
    playlistUris.add(playlistUri);
    }
    protected  ArrayList getPlaylistUris(){
        return  playlistUris;
    }
    protected void addPlaylistImage(Bitmap playlistImage){
        playlistImages.add(playlistImage);
    }
    protected  ArrayList getPlaylistImages(){
        return  playlistImages;
    }
}
