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

import ptit.kl.musicplayer.Model.Artist;
import ptit.kl.musicplayer.R;

public class ArtistAdapter extends ArrayAdapter<Artist> {
    Context context;
    int res;
    ArrayList<Artist> arrArtist;

    public ArtistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Artist> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arrArtist = objects;
        this.res = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(res, parent, false);
        }
        Artist artist = arrArtist.get(position);
        if (artist != null) {

            TextView textViewArtist = (TextView) view.findViewById(R.id.artistname);
            String name = artist.getName();
            if (name.length() <= 30) {
                textViewArtist.setText(name);
            }
            else{
                String tname = name.substring(0,30)+"...";
                textViewArtist.setText(tname);
            }
        }
        return view;

    }
}