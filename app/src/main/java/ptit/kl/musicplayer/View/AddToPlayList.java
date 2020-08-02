package ptit.kl.musicplayer.View;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import ptit.kl.musicplayer.Adapter.AlbumAdapter;
import ptit.kl.musicplayer.Model.Playlist;
import ptit.kl.musicplayer.R;
import ptit.kl.musicplayer.data.PlaylistContract;


public class AddToPlayList extends AppCompatActivity {
    GridView gridViewPlaylist;
    AlbumAdapter albumAdapter;
    LinearLayout linearLayout;
    int songposition;
    EditText editText;
    PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_play_list);
        Intent intent = getIntent();
        songposition = intent.getIntExtra("song", 0);
        gridViewPlaylist = findViewById(R.id.grid_playlist);
        albumAdapter = new AlbumAdapter(this, R.layout.grid_album, AllSongList.arrPlaylist);
        gridViewPlaylist.setAdapter(albumAdapter);
        linearLayout = findViewById(R.id.layout_newalb);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.popup_newalb, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(v, Gravity.CENTER_VERTICAL,0,0);
                Button btnNewAlb = popupView.findViewById(R.id.btnCreateNewAlb);
                editText = popupView.findViewById(R.id.entername);
                btnNewAlb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = 1+ AllSongList.arrPlaylist.size();

                        boolean isOK = false;
                        while(isOK == false){
                            for(int i = 0; i< AllSongList.arrPlaylist.size(); i++){
                                if(id == AllSongList.arrPlaylist.get(i).getIdImg()){
                                    id++;

                                }
                                if(i == AllSongList.arrPlaylist.size()-1){
                                    isOK = true;
                                }
                            }

                        }

                        if (!editText.getText().equals("") || editText.getText() != null){
                            String txt = editText.getText().toString();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(PlaylistContract.AlbumEntry._ID, id);
                            contentValues.put(PlaylistContract.AlbumEntry.PLAYLIST_NAME, txt);
                            contentValues.put(PlaylistContract.AlbumEntry.SONG_POS, songposition);
                            AllSongList.sqlDB.insert(PlaylistContract.AlbumEntry.TABLE_NAME,null,contentValues);
                            Playlist playlist = new Playlist(id, txt);
                            playlist.getmusicInPlaylist().add(AllSongList.mAllSong.get(songposition));
                            AllSongList.arrPlaylist.add(playlist);
                            setResult(2);
                            popupWindow.dismiss();
                            finish();
                        }

                    }
                });
            }
        });
        gridViewPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(PlaylistContract.AlbumEntry._ID, position+1);
                contentValues2.put(PlaylistContract.AlbumEntry.PLAYLIST_NAME, AllSongList.arrPlaylist.get(position).getnamePlaylist());
                contentValues2.put(PlaylistContract.AlbumEntry.SONG_POS, songposition);
                AllSongList.sqlDB.insert(PlaylistContract.AlbumEntry.TABLE_NAME,null,contentValues2);
                AllSongList.arrPlaylist.get(position).getmusicInPlaylist().add(AllSongList.mAllSong.get(songposition));
                setResult(1);
                finish();
            }
        });
    }
}
