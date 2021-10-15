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
        return sort(arr, 0, arr.size());
    }

    private ArrayList<Anime> sort(ArrayList<Anime> arr, int start, int end) {
        ArrayList<Anime> combined = new ArrayList<>();
        if(start != end) {
            int mid = (start + end) / 2;
            ArrayList<Anime> lArr = sort(arr, start, mid);
            ArrayList<Anime> rArr = sort(arr, mid, end);

            boolean hasEnd = false;
            int lIndex = 0;
            int rIndex = 0;
            while(!hasEnd) {
                if(lIndex < lArr.size() && rIndex < rArr.size()) {
                    Anime lVal = lArr.get(lIndex);
                    Anime rVal = rArr.get(rIndex);

                    if(compare(lVal, rVal) > 0) {
                        combined.add(lVal);
                        lIndex++;
                    } else {
                        combined.add(rVal);
                        rIndex++;
                    }
                }
                else if(lIndex < lArr.size() && rIndex >= rArr.size()) {
                    combined.add(lArr.get(lIndex));
                    lIndex++;
                }
                else if(lIndex >= lArr.size() && rIndex < rArr.size()) {
                    combined.add(rArr.get(rIndex));
                    rIndex++;
                } else {
                    hasEnd = true;
                }
            }
        }
        return combined;
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
