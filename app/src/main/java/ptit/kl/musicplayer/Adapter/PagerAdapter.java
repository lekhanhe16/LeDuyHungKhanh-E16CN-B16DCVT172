package ptit.kl.musicplayer.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ptit.kl.musicplayer.View.PlaylistList;
import ptit.kl.musicplayer.View.AllSongList;
import ptit.kl.musicplayer.View.ArtistList;

public class PagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    int numOfTabs;

    public PagerAdapter(Context context, FragmentManager fm, int numOfTabs) {
        super(fm);
        this.context = context;
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllSongList();
            case 1:
                return new ArtistList();
            case 2:
                return new PlaylistList();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
//                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.musicnote);
//                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("     All Songs ");
//                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
//                ImageSpan imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
//                spannableStringBuilder.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                return spannableStringBuilder;
                return "All Songs";
            case 1:
//                Drawable drawable1 = ContextCompat.getDrawable(context,R.drawable.singer);
//                SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder("     Artists ");
//                drawable1.setBounds(0,0,drawable1.getIntrinsicWidth(),drawable1.getIntrinsicHeight());
//                ImageSpan imageSpan1 = new ImageSpan(drawable1,ImageSpan.ALIGN_BASELINE);
//                spannableStringBuilder1.setSpan(imageSpan1,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                return spannableStringBuilder1;
                return "Artists";
            case 2:
//                Drawable drawable2 = ContextCompat.getDrawable(context,R.drawable.musicalbum);
//                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("     Playlists ");
//                drawable2.setBounds(0,0,drawable2.getIntrinsicWidth(),drawable2.getIntrinsicHeight());
//                ImageSpan imageSpan2 = new ImageSpan(drawable2,ImageSpan.ALIGN_BASELINE);
//                spannableStringBuilder2.setSpan(imageSpan2,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                return spannableStringBuilder2;
                return "Playlists";
            default:
                return null;
        }
    }
}
