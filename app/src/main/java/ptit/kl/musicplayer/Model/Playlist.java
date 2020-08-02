package ptit.kl.musicplayer.Model;

import java.util.ArrayList;

public class Playlist {

    int idImg;
    String namePlaylist;
    ArrayList<Song> songInPlaylist;

    public Playlist(int id, String namePlaylist, ArrayList<Song> songInPlaylist){
        this.songInPlaylist = new ArrayList<>();

        this.namePlaylist = namePlaylist;
        this.songInPlaylist = songInPlaylist;
        this.idImg = id;
    }

    public Playlist(int id, String namePlaylist) {

        this.songInPlaylist = new ArrayList<>();
        this.namePlaylist = namePlaylist;

        this.idImg = id;
    }

    public Playlist() {

    }

    public ArrayList<Song> getmusicInPlaylist() {
        return songInPlaylist;
    }

    public String getnamePlaylist() {
        return namePlaylist;
    }

    public void setmusicInPlaylist(ArrayList<Song> songInPlaylist) {
        this.songInPlaylist = songInPlaylist;
    }

    public int getIdImg() {
        return idImg;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }
}
