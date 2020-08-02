package ptit.kl.musicplayer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;

public class SearchAdapter extends ArrayAdapter<Song> {
    public ArrayList<Song> mlistSong;
    public ArrayList<Song> arr;
    public Context context;
    public int Resourld;
    String TAG = "CustomAdapter";

    public SearchAdapter(@NonNull Context context, int resourceAll, @NonNull ArrayList<Song> objectsList) {
        super(context, resourceAll, objectsList);
        this.context = context;
        this.Resourld = resourceAll;
//        Collections.sort(objectsList, new Comparator<Music>() {
//            public int compare(Music a, Music b) {
//                return a.getName().compareTo(b.getName());
//            }
//        });
        this.mlistSong = objectsList;
        arr = new ArrayList<>();
        arr.addAll(mlistSong);
    }


    @Override
    public int getCount() {
        return mlistSong.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(Resourld, parent, false);
        }
        Song song = mlistSong.get(position);
        if (song != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imgIcon);
            TextView textViewName = (TextView) view.findViewById(R.id.songName);
            TextView textViewArtist = (TextView) view.findViewById(R.id.artist);
            imageView.setImageResource(R.drawable.musicnode);
            if (song.getName().length() <= 24) {
                textViewName.setText(song.getName());
            }
            else {
                String tname = song.getName().substring(0,24)+"...";
                textViewName.setText(tname);
            }
            textViewArtist.setText(song.getArtist());
        }
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mlistSong.clear();
        if (charText.length() == 0) {
            mlistSong.addAll(arr);
        } else {
            for (Song wp : arr) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mlistSong.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
