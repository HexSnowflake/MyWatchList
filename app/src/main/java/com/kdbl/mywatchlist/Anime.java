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
            return "";
        return mUrl;
    }

    public int getRating() {
        return mRating;
    }

    public boolean getIsSketch() {
        return mIsSketch;
    }

    public Anime(String title, int rating, boolean isSketch) {
        this(title, rating, isSketch, null);
    }

    public Anime(String title, int rating, boolean isSketch, String url) {
        mTitle = title;
        if(url == null) {
            mUrl = "";
        } else {
            mUrl = url;
        }
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
