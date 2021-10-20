package com.kdbl.mywatchlist;

public class Anime implements Comparable<Anime>{

    private String mTitle;
    private int mRating;
    private boolean mIsSketch;

    public String getTitle() {
        return mTitle;
    }

    public int getRating() {
        return mRating;
    }

    public Anime(String title, int rating, boolean isSketch) {
        mTitle = title;
        mRating = rating;
        mIsSketch = isSketch;
    }

    @Override
    public int compareTo(Anime anime) {
        if(mRating > anime.getRating()) {
            return 1;
        }
        else if(mRating < anime.getRating()) {
            return -1;
        }

        if (mTitle.compareTo(anime.getTitle()) < 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
