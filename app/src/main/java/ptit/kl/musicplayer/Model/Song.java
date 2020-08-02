package ptit.kl.musicplayer.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{
    private String name;
    private String artist;
    private int imgID;
    private String path;
    private String disPlayname;

    public Song(String name, String artist, int id, String path, String disPlayname){
        this.name = name;
        this.artist = artist;
        this.imgID = id;
        this.path = path;
        this.disPlayname = disPlayname;
    }

    protected Song(Parcel in) {
        name = in.readString();
        artist = in.readString();
        imgID = in.readInt();
        path = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getImgID() {
        return imgID;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(artist);
        parcel.writeInt(imgID);
        parcel.writeString(path);
    }

    public String getDisPlayname() {
        return disPlayname;
    }

    public void setDisPlayname(String disPlayname) {
        this.disPlayname = disPlayname;
    }
}
