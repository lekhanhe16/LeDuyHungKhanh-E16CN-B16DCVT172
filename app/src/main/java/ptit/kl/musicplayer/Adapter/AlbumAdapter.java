package ptit.kl.musicplayer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


import ptit.kl.musicplayer.Model.Playlist;
import ptit.kl.musicplayer.R;

public class AlbumAdapter extends ArrayAdapter<Playlist> {
    ArrayList<Playlist> mPlaylist;
    Context context;
    int resID;

    public AlbumAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Playlist> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mPlaylist = objects;
        this.resID = resource;
    }

    @Override
    public int getCount() {
        return mPlaylist.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resID, parent, false);
            Playlist album = mPlaylist.get(position);

            if (album != null) {
                //ImageView imageView =  view.findViewById(R.id.imgAlbum);
                TextView textViewName =  view.findViewById(R.id.albumName);

                //imageView.setImageResource(album.getIdImg());
                textViewName.setText(album.getnamePlaylist());
            }
        }

        return view;

    }
}