package ptit.kl.musicplayer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;

public class CustomAdapter extends ArrayAdapter<Song> {
    public ArrayList<Song> mlistSong;
    MediaMetadataRetriever metaRetriver;
//    public Context context;
    public int Resourld;
    byte[] art;

    public CustomAdapter(@NonNull Context context, int resourceAll, @NonNull ArrayList<Song> objectsList) {
        super(context, resourceAll, objectsList);

        this.Resourld = resourceAll;

        this.mlistSong = objectsList;
        metaRetriver = new MediaMetadataRetriever();
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
        Collections.sort(mlistSong, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getName().compareTo(b.getName());
            }
        });
        Song song = mlistSong.get(position);
        if (song != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imgIcon);
            TextView textViewName = (TextView) view.findViewById(R.id.songName);
            TextView textViewArtist = (TextView) view.findViewById(R.id.artist);

            metaRetriver.setDataSource(song.getPath());
            try {
                art = metaRetriver.getEmbeddedPicture();
                BitmapFactory.Options bfo = new BitmapFactory.Options();
                Bitmap songImage = BitmapFactory
                        .decodeByteArray(art, 0, art.length, bfo);
                imageView.setImageBitmap(songImage);

            } catch (Exception e) {
                imageView.setImageResource(R.drawable.musicnode);
            }
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
}
