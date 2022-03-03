package com.kdbl.mywatchlist;

import java.util.Locale;

public class Anime implements Comparable<Anime>{

    private String mTitle;
    private String mUrl;
    private int mRating;
    private boolean mIsSketch;

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        if(mUrl.isEmpty())
            return null;
        return mUrl;
    }

    public int getRating() {
        return mRating;
    }

    public Anime(String title, int rating, boolean isSketch) {
        this(title, rating, isSketch, "");
    }

    public Anime(String title, int rating, boolean isSketch, String url) {
        mTitle = title;
        mUrl = url;
        mRating = rating;
        mIsSketch = isSketch;
    }

//    where better means greater
    @Override
    public int compareTo(Anime anime) {
        if(mRating > anime.getRating()) {
            return 1;
        }
        else if(mRating < anime.getRating()) {
            return -1;
        }

        if (mTitle.toLowerCase().compareTo(anime.getTitle().toLowerCase()) < 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
