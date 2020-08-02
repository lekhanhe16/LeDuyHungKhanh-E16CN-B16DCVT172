package ptit.kl.musicplayer.Service;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;

import android.os.IBinder;


import android.support.annotation.RequiresApi;


import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import ptit.kl.musicplayer.View.MainActivity;
import ptit.kl.musicplayer.View.MainPlayer;
import ptit.kl.musicplayer.R;
import ptit.kl.musicplayer.notification.MusicNotification;


@SuppressLint("ParcelCreator")
public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    public int position;
    NotificationManager notificationManager;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    public static boolean isShuff = false;
    Vector<Integer> x;
    public MusicService() {
    }
    public MediaPlayer mediaPlayer;
    IBinder iBinder;
    int count = 0;
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        String CHANNEL_ID= this.getResources().getString(R.string.CHANNEL_ID);
        mediaPlayer = new MediaPlayer();
        iBinder = new MyBinder();

//        Intent notiIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notiIntent,0);
//
//        Notification notification = new Notification.Builder(this, CHANNEL_ID)
//                .setContentTitle("Music Player")
//                .setContentText("Music is playing..")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(1,notification);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                metaRetriver = new MediaMetadataRetriever();
                if (isShuff) {
                    if (x == null) {
                        x = new Vector<>();
                    }
                    x.add(position);
                    if (x.size() == MainActivity.arrSongs.size()) {
                        x.clear();
                    }
                    Random r = new Random();
                    int p = r.nextInt(MainActivity.arrSongs.size());
                    while (x.contains(p)) {
                        p = r.nextInt(MainActivity.arrSongs.size());
                    }
                    position = p;
                    try {
                        play1();
                        MainActivity.songTit.setText(MainActivity.arrSongs.get(position).getName());
                        MainActivity.songArtist.setText(MainActivity.arrSongs.get(position).getArtist());
                        MainActivity.btnPlay.setImageResource(R.drawable.play2);
                        MainPlayer.songInfo.setText(MainActivity.arrSongs.get(position).getName()
                                + "-" + MainActivity.arrSongs.get(position).getArtist());
                        metaRetriver.setDataSource(MainActivity.arrSongs.get(position).getPath());
                        try {
                            art = metaRetriver.getEmbeddedPicture();
                            BitmapFactory.Options bfo = new BitmapFactory.Options();
                            Bitmap songImage = BitmapFactory
                                    .decodeByteArray(art, 0, art.length, bfo);
                            MainActivity.imageView.setImageBitmap(songImage);
                            MainPlayer.imageView.setImageBitmap(songImage);

                        } catch (Exception e) {
                            MainActivity.imageView.setImageResource(R.drawable.musicnode);
                            MainPlayer.imageView.setImageResource(R.drawable.musicnode);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (position < MainActivity.arrSongs.size() - 1 && position >= 0) {
                    position++;
                    try {
                        play1();
                        MainActivity.songTit.setText(MainActivity.arrSongs.get(position).getName());
                        MainActivity.songArtist.setText(MainActivity.arrSongs.get(position).getArtist());
                        MainActivity.btnPlay.setImageResource(R.drawable.play2);
                        MainPlayer.songInfo.setText(MainActivity.arrSongs.get(position).getName()
                                + "-" + MainActivity.arrSongs.get(position).getArtist());
                        metaRetriver.setDataSource(MainActivity.arrSongs.get(position).getPath());
                        try {
                            art = metaRetriver.getEmbeddedPicture();
                            BitmapFactory.Options bfo = new BitmapFactory.Options();
                            Bitmap songImage = BitmapFactory
                                    .decodeByteArray(art, 0, art.length, bfo);
                            MainActivity.imageView.setImageBitmap(songImage);
                            MainPlayer.imageView.setImageBitmap(songImage);

                        } catch (Exception e) {
                            MainActivity.imageView.setImageResource(R.drawable.musicnode);
                            MainPlayer.imageView.setImageResource(R.drawable.musicnode);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (position == MainActivity.arrSongs.size() - 1) {
                    position = 0;
                    try {
                        play1();
                        MainActivity.songTit.setText(MainActivity.arrSongs.get(position).getName());
                        MainActivity.songArtist.setText(MainActivity.arrSongs.get(position).getArtist());
                        MainActivity.btnPlay.setImageResource(R.drawable.play2);

                        metaRetriver.setDataSource(MainActivity.arrSongs.get(position).getPath());
                        try {
                            art = metaRetriver.getEmbeddedPicture();
                            BitmapFactory.Options bfo = new BitmapFactory.Options();
                            Bitmap songImage = BitmapFactory
                                    .decodeByteArray(art, 0, art.length, bfo);
                            MainActivity.imageView.setImageBitmap(songImage);
                            MainPlayer.imageView.setImageBitmap(songImage);

                        } catch (Exception e) {
                            MainActivity.imageView.setImageResource(R.drawable.musicnode);
                            MainPlayer.imageView.setImageResource(R.drawable.musicnode);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void createChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(MusicNotification.CHANNEL_ID,
                    "LacTroiMusic", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
    public void playNext() {
        if (isShuff) {
            if (x == null) {
                x = new Vector<>();
            }
            x.add(position);
            if (x.size() == MainActivity.arrSongs.size()) {
                x.clear();
            }
            Random r = new Random();
            int p = r.nextInt(MainActivity.arrSongs.size());
            while (x.contains(p)) {
                p = r.nextInt(MainActivity.arrSongs.size());
            }
            position = p;
            try {
                play1();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (position < MainActivity.arrSongs.size() - 1 && position >= 0) {
            position++;
            try {
                play1();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (position == MainActivity.arrSongs.size() - 1) {
            position = 0;
            try {
                play1();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void playPrevious() {
//        if(isShuff){
//
//        }
//        else if (position <= MainActivity.arrSongs.size() - 1 && position > 0) {
//            position--;
//            try {
//                play1();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (position == 0) {
//            int x = MainActivity.arrSongs.size() - 1;
//            position = x;
//            try {
//                play1();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null & intent.hasExtra("position")) {
            position = intent.getIntExtra("position", 0);
        }
        if (MainActivity.isShuff == true) {
            isShuff = true;
        }
        MainActivity.binder = this.getBinder();
        if (MainActivity.arrSongs.size() > 0) {
            try {
                play1();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void play1() throws IOException {

        mediaPlayer.reset();
        mediaPlayer.setDataSource(MainActivity.arrSongs.get(position).getPath());
        mediaPlayer.prepare();
        mediaPlayer.start();
        MusicNotification.createNotification(getBaseContext(), MainActivity.arrSongs.get(position));

    }

    public void pauseAllSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            MainActivity.btnPlay.setImageResource(R.drawable.pause2);
        }
    }

    public void pause(MainPlayer mainPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mainPlayer.imageButtonPlay.setImageResource(R.drawable.pausebutton);
        }
    }

    public void resume(MainPlayer mainPlayer) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mainPlayer.imageButtonPlay.setImageResource(R.drawable.playbutton);
        }
    }

    public void resumeAllSong() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            MainActivity.btnPlay.setImageResource(R.drawable.play2);
        }
    }

    public MyBinder getBinder() {
        return (MyBinder) iBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }


    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        super.onDestroy();
    }

}
