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
                if(rIndex < end) {
                    Anime rVal = arr.get(rIndex);
                }
            }
        }
        return null;
    }


}
