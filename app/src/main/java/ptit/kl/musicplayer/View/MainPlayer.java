package ptit.kl.musicplayer.View;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;
import ptit.kl.musicplayer.Service.MusicService;

public class MainPlayer extends AppCompatActivity implements Runnable, View.OnClickListener {
    public ImageButton imageButtonOption;
    public ImageButton imageButtonPre, imageButtonPlay, imageButtonNext, imageButtonAdd;
    public static TextView songInfo, artist;
    public static ImageView imageView;
    SeekBar seekBar;
    String name = "";
    Intent intent;
    Runnable mRunnable;
    MusicService musicService;
    ArrayList<Song> arrSong;
    Handler mHandler;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Thread t;
    boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);
        arrSong = MainActivity.arrSongs;
        metaRetriver = new MediaMetadataRetriever();
        intent = getIntent();
        seekBar = findViewById(R.id.seekBar);
        mHandler = new Handler();
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (musicService.mediaPlayer != null && b) {
                    musicService.mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imageButtonPlay = findViewById(R.id.img_btn_play);
        if (intent != null) {
            musicService = MainActivity.binder.getService();

        }
        if (!musicService.mediaPlayer.isPlaying()) {
            imageButtonPlay.setImageResource(R.drawable.pausebutton);
        }
        artist = this.findViewById(R.id.artistplayer);
        songInfo = this.findViewById(R.id.textView);
        initializeSeekBar();

        imageButtonOption = findViewById(R.id.btnOp);
        imageButtonOption.setOnClickListener(this);
        imageButtonAdd = findViewById(R.id.btnAdd);
        imageButtonAdd.setOnClickListener(this);
        imageButtonNext = findViewById(R.id.img_btn_next);
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.playNext();
                isRunning = false;
                imageButtonPlay.setImageResource(R.drawable.playbutton);
                MainActivity.songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                MainActivity.songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
                try {
                    art = metaRetriver.getEmbeddedPicture();
                    BitmapFactory.Options bfo = new BitmapFactory.Options();
                    Bitmap songImage = BitmapFactory
                            .decodeByteArray(art, 0, art.length, bfo);
                    imageView.setImageBitmap(songImage);

                } catch (Exception e) {
                    imageView.setImageResource(R.drawable.musicnode);
                }
                try {
                    seekBar.setProgress(0);
                    mHandler = new Handler();
                    musicService.play1();
                    initializeSeekBar();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.mediaPlayer.isPlaying()) {
                    musicService.pause(MainPlayer.this);

                    MainActivity.btnPlay.setImageResource(R.drawable.pause2);
                } else {
                    musicService.resume(MainPlayer.this);
                    MainActivity.btnPlay.setImageResource(R.drawable.play2);

                }

            }
        });
        imageButtonPre = findViewById(R.id.img_btn_pre);
        imageButtonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning = false;
                if (musicService.position <= MainActivity.arrSongs.size() - 1 && musicService.position > 0) {
                    musicService.position--;

                    MainActivity.songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                    MainActivity.songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                    MainActivity.btnPlay.setImageResource(R.drawable.play2);
                    imageButtonPlay.setImageResource(R.drawable.playbutton);
                    metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
                    try {
                        art = metaRetriver.getEmbeddedPicture();
                        BitmapFactory.Options bfo = new BitmapFactory.Options();
                        Bitmap songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length, bfo);
                        imageView.setImageBitmap(songImage);

                    } catch (Exception e) {
                        imageView.setImageResource(R.drawable.musicnode);
                    }
                    try {
                        seekBar.setProgress(0);
                        mHandler = new Handler();
                        musicService.play1();
                        initializeSeekBar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (musicService.position == 0) {
                    int x = MainActivity.arrSongs.size() - 1;
                    musicService.position = x;

//
                    MainActivity.songTit.setText(MainActivity.arrSongs.get(musicService.position).getName());
                    MainActivity.songArtist.setText(MainActivity.arrSongs.get(musicService.position).getArtist());
                    MainActivity.btnPlay.setImageResource(R.drawable.play2);
                    imageButtonPlay.setImageResource(R.drawable.playbutton);
                    metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
                    try {
                        art = metaRetriver.getEmbeddedPicture();
                        BitmapFactory.Options bfo = new BitmapFactory.Options();
                        Bitmap songImage = BitmapFactory
                                .decodeByteArray(art, 0, art.length, bfo);
                        imageView.setImageBitmap(songImage);

                    } catch (Exception e) {
                        imageView.setImageResource(R.drawable.musicnode);
                    }
                    try {
                        seekBar.setProgress(0);
                        mHandler = new Handler();
                        musicService.play1();
                        initializeSeekBar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        imageView = findViewById(R.id.imageView);
        metaRetriver.setDataSource(MainActivity.arrSongs.get(musicService.position).getPath());
        try {
            art = metaRetriver.getEmbeddedPicture();
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(art, 0, art.length, bfo);
            imageView.setImageBitmap(songImage);

        } catch (Exception e) {
            imageView.setImageResource(R.drawable.musicnode);
        }
    }

    public void initializeSeekBar() {
        seekBar.setMax(musicService.mediaPlayer.getDuration() / 1000);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (musicService.mediaPlayer != null) {
                    int mCurrentPosition = musicService.mediaPlayer.getCurrentPosition() / 1000; // In milliseconds
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
        name = MainActivity.arrSongs.get(musicService.position).getName();
        String art = MainActivity.arrSongs.get(musicService.position).getArtist();
        if (name.length() > 20) {
            name = "      " + name;
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String tmp = "";
                    isRunning = true;
                    int index = 0;
                    while (isRunning) {
                        tmp += name.charAt(index);
                        if (tmp.length() > 13) {
                            tmp = tmp.substring(1);
                        }
                        songInfo.setText(tmp);
                        if (index == name.length() - 1) {
                            index = 0;
                        } else {
                            index++;
                        }
                        try {
                            Thread.sleep(84);
                        } catch (InterruptedException ex) {

                        }
                    }
                }
            });
            t.start();
        } else {
            songInfo.setText(name);
        }
        artist.setText(art);
    }

    @Override
    public void onClick(View view) {
        if (view == imageButtonOption) {

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
            MainActivity.main.onResume();
            isRunning = false;
            //t.interrupt();
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        isRunning = false;
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
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
    public void run() {

    }
}