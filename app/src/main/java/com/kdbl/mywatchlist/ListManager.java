package com.kdbl.mywatchlist;

import java.util.ArrayList;

public class ListManager
{
//    temporarily made public & static for testing

    public ArrayList<Anime> sort(ArrayList<Anime> arr) {
        if(arr.size() <= 1) {
            return  arr;
        }

        ArrayList<Anime> l1 = copy(arr, 0, arr.size() / 2);
        ArrayList<Anime> l2 = copy(arr, arr.size() / 2, arr.size());

        l1 = sort(l1);
        l2 = sort(l2);

        return combine(l1, l2);
    }

    private ArrayList<Anime> combine(ArrayList<Anime> left, ArrayList<Anime> right) {
        ArrayList<Anime> combined = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        while(leftIndex < left.size() || rightIndex < right.size()) {
            if(leftIndex < left.size() && rightIndex < right.size()) {
                Anime lVal = left.get(leftIndex);
                Anime rVal = right.get(rightIndex);

                if(lVal.compareTo(rVal) > 0) {
                    combined.add(lVal);
                    leftIndex++;
                } else {
                    combined.add(rVal);
                    rightIndex++;
                }
            }
            else if(leftIndex < left.size()) {
                combined.add(left.get(leftIndex));
                leftIndex++;
            }
            else {
                combined.add(right.get(rightIndex));
                rightIndex++;
            }
        }
        return combined;
    }

    private ArrayList<Anime> copy(ArrayList<Anime> list, int start, int end) {
        if(start < 0 || start > list.size() || end > list.size()) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        ArrayList<Anime> r = new ArrayList<>();
        for(int i = start; i < end; i++) {
            r.add(list.get(i));
        }
        return r;
    }


}
