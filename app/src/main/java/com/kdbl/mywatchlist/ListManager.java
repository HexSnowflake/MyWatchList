package com.kdbl.mywatchlist;

import java.util.ArrayList;

public class ListManager
{
    private class Anime {

        String mTitle;
        int mRating;
        boolean mIsSketch;

        public Anime(String title, int rating, boolean isSketch) {
            mTitle = title;
            mRating = rating;
            mIsSketch = isSketch;
        }
    }

    public ArrayList<Anime> sort(ArrayList<Anime> arr) {
        ArrayList<Anime> r = new ArrayList<>();
        sort(arr, r, 0, arr.size());
        return r;
    }

    private ArrayList<Anime> sort(ArrayList<Anime> arr, ArrayList<Anime> r, int start, int end) {
        if(start != end) {
            int mid = (start + end) / 2;
            sort(arr, r, start, mid);
            sort(arr, r, mid, end);

            for(int i = start; i < mid; i++) {
                Anime lVal = arr.get(i);
                int rIndex = i - start + mid;
                Anime rVal = null;
                if(rIndex < end) {
                    rVal = arr.get(rIndex);
                }

                if(compare(lVal, rVal) > 0) {

                }
            }
        }
        return null;
    }

    private int compare(Anime lVal, Anime rVal) {
        if(lVal.mRating > rVal.mRating) {
            return 1;
        }
        else if(lVal.mRating < rVal.mRating) {
            return -1;
        }

        if (lVal.mTitle.compareTo(rVal.mTitle) < 0) {
            return 1;
        }else {
            return -1;
        }
    }


}
