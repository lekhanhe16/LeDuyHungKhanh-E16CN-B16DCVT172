package ptit.kl.musicplayer.View;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ptit.kl.musicplayer.Adapter.CustomAdapter;
import ptit.kl.musicplayer.Model.Playlist;
import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;
import ptit.kl.musicplayer.Service.MusicService;
import ptit.kl.musicplayer.data.DbHelper;
import ptit.kl.musicplayer.data.PlaylistContract;


public class AllSongList extends Fragment {

    public AllSongList() {
        // Required empty public constructor

    }
    MediaPlayer mediaPlayer;
    ListView allSong;
    public static ArrayList<Song> mAllSong;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    CustomAdapter customAdapter;
    ContentResolver contentResolver;
    Uri musicUri;
    Cursor musicCursor;
    ArtistList artistList;
    public static SQLiteDatabase sqlDB = null;
    public static ArrayList<Playlist> arrPlaylist ;
    public MusicService musicService;
    PopupWindow popupWindow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        metaRetriver = new MediaMetadataRetriever();
        mediaPlayer = new MediaPlayer();
        artistList = new ArtistList();
        arrPlaylist = new ArrayList<>();


        //musicService = new MusicService();
        final View view = inflater.inflate(R.layout.fragment_all__song, container, false);
        allSong = view.findViewById(R.id.lv_all_song);
        mAllSong = new ArrayList<>();

        mAllSong = getAllSong();
        MainActivity.arrSongs = mAllSong;
        customAdapter = new CustomAdapter(getContext(), R.layout.song, mAllSong);
        allSong.setAdapter(customAdapter);
        sqlDB = new DbHelper(getContext()).getReadableDatabase();
        allSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // musicService = MainActivity.binder.getService();
                if (musicService == null) {
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    intent.putExtra("position", arg2);
                    getActivity().bindService(intent, serviceConnection, getContext().BIND_AUTO_CREATE);
                    getActivity().startService(intent);

                    MainActivity.relativeLayout.setVisibility(RelativeLayout.VISIBLE);
                    MainActivity.songTit.setText(MainActivity.arrSongs.get(arg2).getName());
                    MainActivity.songArtist.setText(MainActivity.arrSongs.get(arg2).getArtist());
                    MainActivity.btnPlay.setImageResource(R.drawable.play2);
                    metaRetriver.setDataSource(MainActivity.arrSongs.get(arg2).getPath());
                    try {
                        art = metaRetriver.getEmbeddedPicture();
                        BitmapFactory.Options bfo = new BitmapFactory.Options();
                        Bitmap songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length, bfo);
                        MainActivity.imageView.setImageBitmap(songImage);

                    } catch (Exception e) {
                        MainActivity.imageView.setImageResource(R.drawable.musicnode);
                    }
                } else {
                    musicService = MainActivity.binder.getService();
                    musicService.position = arg2;
                    try {
                        musicService.play1();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MainActivity.relativeLayout.setVisibility(RelativeLayout.VISIBLE);
                    MainActivity.songTit.setText(MainActivity.arrSongs.get(arg2).getName());
                    MainActivity.songArtist.setText(MainActivity.arrSongs.get(arg2).getArtist());
                    MainActivity.btnPlay.setImageResource(R.drawable.play2);
                    metaRetriver.setDataSource(MainActivity.arrSongs.get(arg2).getPath());
                    try {
                        art = metaRetriver.getEmbeddedPicture();
                        BitmapFactory.Options bfo = new BitmapFactory.Options();
                        Bitmap songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length, bfo);
                        MainActivity.imageView.setImageBitmap(songImage);

                    } catch (Exception e) {
                        MainActivity.imageView.setImageResource(R.drawable.musicnode);
                    }
                }
            }

        });
        String[] projection = {
                PlaylistContract.AlbumEntry._ID,
                PlaylistContract.AlbumEntry.PLAYLIST_NAME,
                PlaylistContract.AlbumEntry.SONG_POS
        };
        Cursor cursor = sqlDB.query(PlaylistContract.AlbumEntry.TABLE_NAME, projection, null,
                null,null,null,PlaylistContract.AlbumEntry._ID);

        while (cursor.moveToNext()){
            if(arrPlaylist.size() == 0 || arrPlaylist.get(arrPlaylist.size()-1).getIdImg() != cursor.getInt(0)){
                Playlist album = new Playlist(cursor.getInt(0), cursor.getString(1));
                arrPlaylist.add(album);

                arrPlaylist.get(arrPlaylist.size()-1).getmusicInPlaylist().add(mAllSong.get(cursor.getInt(2)));

            }
            else if (arrPlaylist.get(arrPlaylist.size()-1).getIdImg() == cursor.getInt(0)) {
                arrPlaylist.get(cursor.getInt(0)-1).getmusicInPlaylist().
                        add(mAllSong.get(cursor.getInt(2)));
            }

        }
        Log.d("myplaysize", arrPlaylist.size()+"");
        allSong.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater inflater = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_menu, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL,0,0);
                Button btnDelete = popupView.findViewById(R.id.delete_song);
                Button addToPlaylist = popupView.findViewById(R.id.add_to_playlist);
                Button btnSearch = popupView.findViewById(R.id.search_popup);

                addToPlaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), AddToPlayList.class);
                        intent.putExtra("song", position);
                        startActivityForResult(intent,1);
                    }
                });
                
                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), WebSearch.class);
                        intent.putExtra("song", mAllSong.get(position).getName()+" "+ mAllSong.get(position).getArtist());
                        startActivity(intent);
                    }
                });
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode ==1 || resultCode ==2){
            popupWindow.dismiss();
        }
    }

    public ArrayList<Song> getAllSong() {
        ArrayList<Song> arrayList = new ArrayList<>();
        contentResolver = getActivity().getContentResolver();
        musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        musicCursor = contentResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {


            do {
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int path = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.DATA);
                int fileName = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.DISPLAY_NAME);

                int thisId = (int) musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisPath = musicCursor.getString(path);
                String thisDis = musicCursor.getString(fileName);
                Log.d("displayname", thisDis +", "+ thisTitle);
                arrayList.add(new Song(thisTitle, thisArtist, thisId, thisPath, thisDis));
            }
            while (musicCursor.moveToNext());
        }
        Collections.sort(arrayList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getName().compareTo(b.getName());
            }
        });
        return arrayList;
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder myBinder = (MusicService.MyBinder) iBinder;
            musicService = myBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

        @Override
        public void onBindingDied(ComponentName name) {

        }
    };
}
