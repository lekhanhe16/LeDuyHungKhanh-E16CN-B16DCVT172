package ptit.kl.musicplayer.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ptit.kl.musicplayer.Adapter.ArtistAdapter;
import ptit.kl.musicplayer.Model.Artist;
import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;


public class ArtistList extends Fragment {

    public ArtistList() {
        // Required empty public constructor
    }

    ListView artist;
    ArrayList<Artist> arrAtist;
    ArtistAdapter artistAdapter;
    ArrayList<Song> arrSong;
    Bundle b;
    int size;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist__list, container, false);
        artist = view.findViewById(R.id.lv_artist);

        arrAtist = new ArrayList<>();

        arrSong = new ArrayList<>();

        arrSong = MainActivity.arrSongs;
        Collections.sort(arrSong, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getArtist().compareTo(b.getArtist());
            }
        });

        ArrayList<Song> artistSong = new ArrayList<>();
        for(int i = 0; i< arrSong.size(); i++){
            if(i==0){
                Artist artist = new Artist();
                artist.setName(arrSong.get(i).getArtist());
                artist.mArtMuisic = new ArrayList<>();
                artist.mArtMuisic.add(arrSong.get(i));
                arrAtist.add(artist);
            }
            else if(!arrSong.get(i).getArtist().equalsIgnoreCase(arrAtist.get(arrAtist.size()-1).getName())){
                Artist artist = new Artist();
                artist.setName(arrSong.get(i).getArtist());
                artist.mArtMuisic = new ArrayList<>();
                artist.mArtMuisic.add(arrSong.get(i));
                arrAtist.add(artist);
            }
            else {
                arrAtist.get(arrAtist.size()-1).mArtMuisic.add(arrSong.get(i));
            }
        }
        artistAdapter = new ArtistAdapter(getContext(),R.layout.artist,arrAtist);
        artist.setAdapter(artistAdapter);
        artist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Song> arrM = arrAtist.get(i).getmArtMuisic();
                Intent intent = new Intent(getContext(),ListMusic.class);
                intent.putParcelableArrayListExtra("listm",arrM);
                startActivityForResult(intent,1);

            }
        });
        return view;
    }


}
