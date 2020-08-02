package ptit.kl.musicplayer.Model;

import java.util.ArrayList;

public class Artist {

    String name;
    public ArrayList<Song> mArtMuisic;
    public Artist( String name){

        this.name = name;
    }
    public Artist( String name, ArrayList<Song> arrmusic){

        this.name = name;
        this.mArtMuisic = arrmusic;
    }
    public Artist(){

    }
    public String getName() {
        return name;
    }

    public ArrayList<Song> getmArtMuisic() {
        return mArtMuisic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setmArtMuisic(ArrayList<Song> mArtMuisic) {
        this.mArtMuisic = mArtMuisic;
    }
}
