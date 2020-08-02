package ptit.kl.musicplayer.data;

import android.provider.BaseColumns;

public final class PlaylistContract {
    private PlaylistContract(){

    }
    public static final class AlbumEntry implements BaseColumns{
        public final static String TABLE_NAME = "playlist";
        public final static String _ID = BaseColumns._ID;
        public final static String PLAYLIST_NAME = "name";
        public final static String SONG_POS = "position";
    }
}
