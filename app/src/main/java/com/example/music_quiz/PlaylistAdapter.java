package com.example.music_quiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.spotify.protocol.types.ListItem;
import com.spotify.protocol.types.ListItems;

import java.util.ArrayList;

public class PlaylistAdapter extends ArrayAdapter{

    ListItems playlists;
    ArrayList<Bitmap> playlistImages;
    int custom_layout_id;

    public PlaylistAdapter(@NonNull Context context, int resource, ListItems playlistsList, ArrayList playlistImages) {
        super(context, resource);
        playlists = playlistsList;
        this.playlistImages = playlistImages;
        custom_layout_id = resource;
    }
    @Override public int getCount()
    {
        return playlists.total;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        ImageView playlistImage = convertView.findViewById(R.id.playlistImage);
        TextView playlistTitle = convertView.findViewById(R.id.playlistTitle);
            Log.d("Image Object position", String.valueOf(position));
            Log.d("Image Object length", String.valueOf(playlists.items.length));
        if(playlists.items.length > position) {
            Log.d("Image Object", String.valueOf(playlistImages.get(position)));
            playlistImage.setImageBitmap(playlistImages.get(position));
            ListItem playlist = playlists.items[position];
            playlistTitle.setText(playlist.title);
        }
        return convertView;
    }
}
