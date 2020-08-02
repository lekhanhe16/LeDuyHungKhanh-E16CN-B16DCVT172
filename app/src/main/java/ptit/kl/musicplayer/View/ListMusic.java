package ptit.kl.musicplayer.View;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import ptit.kl.musicplayer.Adapter.CustomAdapter;
import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;
import ptit.kl.musicplayer.Service.MusicService;

public class ListMusic extends AppCompatActivity {
    ListView listView;
    CustomAdapter customAdapter;
    MusicService musicService;
    MediaMetadataRetriever metaRetriver;
    TextView playListName;
    byte[] art;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);

        Intent i = getIntent();
        ArrayList<Song> arr = i.getParcelableArrayListExtra("listm");
        String playlistName = i.getStringExtra("playlistname");
        playListName = findViewById(R.id.playListName);
        playListName.setText(playlistName);
        listView = findViewById(R.id.listmusic);
        customAdapter = new CustomAdapter(ListMusic.this,R.layout.song,arr);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                metaRetriver = new MediaMetadataRetriever();
                if(MainActivity.binder != null){
                    MusicService.MyBinder bind = MainActivity.binder;
                    musicService = bind.getService();
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
                        metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
                        try {
                            art = metaRetriver.getEmbeddedPicture();
                            BitmapFactory.Options bfo=new BitmapFactory.Options();
                            Bitmap songImage = BitmapFactory
                                    .decodeByteArray(art, 0, art.length,bfo);
                            MainActivity.imageView.setImageBitmap(songImage);

                        } catch (Exception e) {
                            MainActivity.imageView.setImageResource(R.drawable.musicnode);
                        }
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    musicService = new MusicService();
                    Intent intent = new Intent(ListMusic.this, MusicService.class);
                    Song m = (Song) listView.getItemAtPosition(i);
                    String name = m.getName();
                    int x = 0;
                    for(int it = 0; it<MainActivity.arrSongs.size(); it++){
                        if(name.equals(MainActivity.arrSongs.get(it).getName())){
                            x = it;
                            break;
                        }
                    }
                    intent.putExtra("position", x);

                    MainActivity.binder = musicService.getBinder();
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                    startService(intent);
                    MainActivity.songArtist.setText(MainActivity.arrSongs.get(x).getArtist());

                    MainActivity.songTit.setText(MainActivity.arrSongs.get(x).getName());
                    MainActivity.relativeLayout.setVisibility(RelativeLayout.VISIBLE);
                    MainActivity.btnPlay.setImageResource(R.drawable.play2);
                    metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
                    try {
                        art = metaRetriver.getEmbeddedPicture();
                        BitmapFactory.Options bfo=new BitmapFactory.Options();
                        Bitmap songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length,bfo);
                        MainActivity.imageView.setImageBitmap(songImage);

                    } catch (Exception e) {
                        MainActivity.imageView.setImageResource(R.drawable.musicnode);
                    }
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


}
