package ptit.kl.musicplayer.View;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.Adapter.PagerAdapter;
import ptit.kl.musicplayer.R;
import ptit.kl.musicplayer.Service.MusicService;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Song> arrSongs;
    public static int REQUEST_READ_EXT = 1;
    public static MainActivity main;
    public static MusicService.MyBinder binder;
    public static TextView songTit, songArtist;
    public static ImageButton btnPre, btnNext, btnPlay;
    MusicService musicService;

    public static ImageView imageView;
    ImageButton btnSearch;
    public static RelativeLayout relativeLayout;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    public static boolean isShuff;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isShuff = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                viewPager = findViewById(R.id.viewPager1);
                pagerAdapter = new PagerAdapter(MainActivity.this, getSupportFragmentManager(), 3);
                viewPager.setAdapter(pagerAdapter);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXT);
            }
        }
        musicService = new MusicService();
        metaRetriver = new MediaMetadataRetriever();
        btnPlay = findViewById(R.id.playInbar);
        songTit = findViewById(R.id.titInbar);
        songArtist = findViewById(R.id.artistInbar);
        imageView = findViewById(R.id.imgInbar);
        main = this;
        relativeLayout = findViewById(R.id.rl1);
        relativeLayout.setVisibility(RelativeLayout.GONE);

        try {
            if (MainActivity.binder.getService() != null) {
                musicService = MainActivity.binder.getService();
                MainActivity.songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                MainActivity.songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                MainActivity.btnPlay.setImageResource(R.drawable.play2);
                MainActivity.relativeLayout.setVisibility(RelativeLayout.VISIBLE);
            }
        } catch (NullPointerException e) {

        }

        arrSongs = new ArrayList<>();
        btnSearch = findViewById(R.id.actionbarBtnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });


        songTit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainPlayer.class);
                startActivity(intent);
            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                musicService = binder.getService();
                if (musicService.mediaPlayer.isPlaying()) {
                    musicService.pauseAllSong();
                } else {
                    musicService.resumeAllSong();
                }

            }
        });
        btnNext = findViewById(R.id.nextInbar);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService = binder.getService();
                musicService.playNext();
                songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                    songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                    btnPlay.setImageResource(R.drawable.play2);
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
            }
        });
        btnPre = findViewById(R.id.preInbar);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService = binder.getService();
                if (musicService.position <= MainActivity.arrSongs.size() - 1 && musicService.position > 0) {
                    musicService.position--;
                    songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                    songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                    btnPlay.setImageResource(R.drawable.play2);
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
                    try {
                        musicService.play1();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (musicService.position == 0) {
                    int x = MainActivity.arrSongs.size() - 1;
                    musicService.position = x;

                    btnPlay.setImageResource(R.drawable.play2);
                    songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                    songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
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
                    try {
                        musicService.play1();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ImageButton btnShuffle = findViewById(R.id.btnShuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isShuff) {
                    Toast.makeText(MainActivity.this, "Shuffle Mode: ON", Toast.LENGTH_LONG).show();

                    Random r = new Random();

                    int arg2 = r.nextInt(MainActivity.arrSongs.size());
                    MainActivity.isShuff = true;
                    Intent intent = new Intent(MainActivity.this, MusicService.class);
                    intent.putExtra("position", arg2);
                    MainActivity.this.bindService(intent, serviceConnection, getBaseContext().BIND_AUTO_CREATE);
                    MainActivity.this.startService(intent);
                    //MusicService.isShuff = true;

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
                    MainActivity.isShuff = true;

                } else {
                    musicService = binder.getService();
                    MusicService.isShuff = false;
                    MainActivity.isShuff = false;
                    Toast.makeText(MainActivity.this, "Shuffle Mode: OFF", Toast.LENGTH_LONG).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_EXT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                viewPager = findViewById(R.id.viewPager1);
                pagerAdapter = new PagerAdapter(MainActivity.this, getSupportFragmentManager(), 3);
                viewPager.setAdapter(pagerAdapter);
            }
            else {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
