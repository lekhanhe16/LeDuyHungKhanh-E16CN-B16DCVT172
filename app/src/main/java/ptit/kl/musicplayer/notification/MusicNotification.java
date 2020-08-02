package ptit.kl.musicplayer.notification;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import ptit.kl.musicplayer.Model.Song;
import ptit.kl.musicplayer.R;

public class MusicNotification {
    public static final String CHANNEL_ID = "channel1";
    public static final String CHANNEL_PREVIOUS = "actionprevious";
    public static final String CHANNEL_PLAY = "actionplay";
    public static final String CHANNEL_NEXT = "actionnext";
    public static MediaMetadataRetriever metaRetriver;
    public static Notification notification;
    public static BitmapFactory.Options bfo = new BitmapFactory.Options();


    public static void createNotification(Context context, Song m){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "aaa");
            if(metaRetriver ==null){
                metaRetriver = new MediaMetadataRetriever();
            }
            metaRetriver.setDataSource(m.getPath());
            byte[] art = metaRetriver.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(art, 0, art.length, bfo);

            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.musicnode)
                    .setContentTitle(m.getName())
                    .setContentText(m.getArtist())
                    .setLargeIcon(songImage)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            notificationManagerCompat.notify(1, notification);
        }
    }
}
