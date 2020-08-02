package ptit.kl.musicplayer.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import ptit.kl.musicplayer.Adapter.AlbumAdapter;
import ptit.kl.musicplayer.R;


public class PlaylistList extends Fragment {

    GridView gridView;
    AlbumAdapter albumAdapter;

    public PlaylistList() {
        // Required empty public constructor
        Log.d("arr playlist size", AllSongList.arrPlaylist.get(0).getmusicInPlaylist().size()+"\n");
        Log.d("arr playlist 1 size", AllSongList.arrPlaylist.get(1).getmusicInPlaylist().size()+"");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);
        gridView = view.findViewById(R.id.view_album);
        albumAdapter = new AlbumAdapter(getContext(),R.layout.grid_album, AllSongList.arrPlaylist);
        gridView.setAdapter(albumAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ListMusic.class);
                intent.putParcelableArrayListExtra("listm", AllSongList.arrPlaylist.get(position).getmusicInPlaylist());
                intent.putExtra("playlistname", AllSongList.arrPlaylist.get(position).getnamePlaylist());
                startActivity(intent);
            }
        });
        return view;
    }


}
