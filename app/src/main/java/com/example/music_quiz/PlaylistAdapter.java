package com.example.music_quiz;

import android.content.Context;
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

//public class PlaylistAdapter extends BaseAdapter {
//
//    private Context context;
//    private LayoutInflater inflater;
//    private ArrayList title;
//    private ArrayList imageUri;
//
//    public PlaylistAdapter(Context c, ArrayList title, ArrayList imageUri){
//        context = c;
//        this.title = title;
//        this.imageUri = imageUri;
//    }
//    @Override
//    public int getCount() {
//        return title.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if(inflater == null){
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//        if(convertView == null){
//            convertView = inflater.inflate(R.layout.row_item, null);
//        }
//        ImageView playlistImage = convertView.findViewById(R.id.playlistImage);
//        TextView playlistTitle = convertView.findViewById(R.id.playlistTitle);
//
//        playlistImage.setImageResource((Integer) imageUri.get(position));
//        playlistTitle.setText((Integer) title.get(position));
//
//        return convertView;
//    }
//}

public class PlaylistAdapter extends ArrayAdapter{

    ListItems playlists;
    int custom_layout_id;

    public PlaylistAdapter(@NonNull Context context, int resource, ListItems playlistsList) {
        super(context, resource);
        playlists = playlistsList;
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
        ListItem playlist = playlists.items[position];
        playlistTitle.setText(playlist.title);
        return convertView;
    }
}
