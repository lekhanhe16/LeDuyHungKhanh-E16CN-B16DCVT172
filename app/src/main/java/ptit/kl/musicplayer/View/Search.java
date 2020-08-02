package ptit.kl.musicplayer.View;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;
import  android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ptit.kl.musicplayer.Adapter.SearchAdapter;
import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;
import ptit.kl.musicplayer.Service.MusicService;

public class Search extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    private ListView listView;
    private SearchAdapter customAdapter;
    MusicService musicService;
    MediaMetadataRetriever metaRetriver;
    byte[] art;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.list_view_search);
        searchView = findViewById(R.id.search_view);
        customAdapter = new SearchAdapter(Search.this, R.layout.song, MainActivity.arrSongs);
        listView.setAdapter(customAdapter);
        searchView.setOnQueryTextListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.arrSongs = getAllSong();
                metaRetriver = new MediaMetadataRetriever();
                if (MainActivity.binder != null) {
                    MusicService.MyBinder bind = MainActivity.binder;
                    musicService = bind.getService();
                    //musicService.position = i;
                    Song m = (Song) listView.getItemAtPosition(i);
                    String name = m.getName();

                    for(int it = 0; it<MainActivity.arrSongs.size(); it++){
                        if(name.equals(MainActivity.arrSongs.get(it).getName())){
                            musicService.position = it;
                            break;
                        }
                    }
                    try {
                        musicService.play1();
                        MainActivity.songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                        MainActivity.songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                        MainActivity.relativeLayout.setVisibility(RelativeLayout.VISIBLE);
                        MainActivity.btnPlay.setImageResource(R.drawable.play2);
                        customAdapter.mlistSong.clear();
                        customAdapter.mlistSong.addAll(MainActivity.arrSongs);
                        metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
                        try {
                            art = metaRetriver.getEmbeddedPicture();
                            BitmapFactory.Options bfo = new BitmapFactory.Options();
                            Bitmap songImage = BitmapFactory
                                    .decodeByteArray(art, 0, art.length, bfo);
                            MainActivity.imageView.setImageBitmap(songImage);

                        } catch (Exception e) {
                            MainActivity.imageView.setImageResource(R.drawable.musicnode);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    customAdapter.mlistSong.clear();
                    customAdapter.mlistSong.addAll(MainActivity.arrSongs);
                    finish();
                } else {
                    musicService = new MusicService();
                    Intent intent = new Intent(Search.this, MusicService.class);
                    Song m = (Song) listView.getItemAtPosition(i);
                    String name = m.getName();
                    int x = 0;
                    for(int it = 0; it<MainActivity.arrSongs.size(); it++){
                        if(name.equals(MainActivity.arrSongs.get(it).getName())){
                            x = it;
                            musicService.position = x;
                            break;
                        }
                    }
                    intent.putExtra("position", x);
                    MainActivity.binder = musicService.getBinder();
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                    startService(intent);
                    MainActivity.songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                    MainActivity.songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                    MainActivity.relativeLayout.setVisibility(RelativeLayout.VISIBLE);
                    MainActivity.btnPlay.setImageResource(R.drawable.play2);
                    metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
                    try {
                        art = metaRetriver.getEmbeddedPicture();
                        BitmapFactory.Options bfo = new BitmapFactory.Options();
                        Bitmap songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length, bfo);
                        MainActivity.imageView.setImageBitmap(songImage);

                    } catch (Exception e) {
                        MainActivity.imageView.setImageResource(R.drawable.musicnode);
                    }
                    customAdapter.mlistSong.clear();
                    customAdapter.mlistSong.addAll(MainActivity.arrSongs);
                    finish();
                }
            }
        });
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        customAdapter.filter(s);
        return false;
    }
    public ArrayList<Song> getAllSong() {
        ContentResolver contentResolver;
        Uri musicUri;
        Cursor musicCursor;
        ArrayList<Song> arrayList = new ArrayList<>();
        contentResolver = getBaseContext().getContentResolver();
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
}
