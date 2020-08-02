package ptit.kl.musicplayer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lactroi.db";
    private static final int DATABASE_VERSION = 2;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + PlaylistContract.AlbumEntry.TABLE_NAME + " ( " +
                PlaylistContract.AlbumEntry._ID +" INTEGER , " +
                "name TEXT NOT NULL, " +
                PlaylistContract.AlbumEntry.SONG_POS + " INTEGER NOT NULL, PRIMARY KEY (_ID, position));";
        db.execSQL(query);

        ContentValues contentValues = new ContentValues();

        contentValues.put(PlaylistContract.AlbumEntry._ID, 1);
        contentValues.put(PlaylistContract.AlbumEntry.PLAYLIST_NAME, "POP");
        contentValues.put(PlaylistContract.AlbumEntry.SONG_POS, 0);

        db.insert(PlaylistContract.AlbumEntry.TABLE_NAME,null,contentValues);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(PlaylistContract.AlbumEntry._ID, 2);
        contentValues2.put(PlaylistContract.AlbumEntry.PLAYLIST_NAME, "EDM");
        contentValues2.put(PlaylistContract.AlbumEntry.SONG_POS, 1);

        db.insert(PlaylistContract.AlbumEntry.TABLE_NAME,null,contentValues2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
