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
                Anime lVal = null;
                if(lIndex < lArr.size())
                    lArr.get(lIndex);
                Anime rVal = null;
                if(rIndex < rArr.size())
                    rArr.get(rIndex);

                if(compare(lVal, rVal) > 0) {
                    combined.add(lVal);
                    lIndex++;
                }
                else if(compare(lVal, rVal) < 0) {
                    combined.add(rVal);
                    rIndex++;
                } else {
                    if(lVal == null) {
                        if(rVal == null) {
                            hasEnd = true;
                        } else {
                            combined.add(rVal);
                            rIndex++;
                        }
                    }
                    if(rVal == null) {
                        if(lVal == null) {
                            hasEnd = true;
                        } else {
                            combined.add(lVal);
                            rIndex++;
                        }
                    }
                }
            }
        }
        return combined;
    }

    private int compare(Anime lVal, Anime rVal) {
        if(lVal == null || rVal == null) {
            return 0;
        }

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
