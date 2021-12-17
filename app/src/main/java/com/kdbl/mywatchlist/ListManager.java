package com.kdbl.mywatchlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ListManager
{
    private static ListManager instance = null;
//    private List<Anime> mAnimeList = new ArrayList<>();
//    private Map<String, Integer> mAnimeMap = new HashMap<>();
//    TODO: The set still needs to be updated when things are changed
    private Set<String> mAnimeSet = new HashSet<>();

    protected static WatchListOpenHelper mDbHelper = null;

    //    make singleton
    public static ListManager getInstance() {
        if(instance == null) {
            instance = new ListManager();
        }
        return instance;
    }

    public static void deleteFromDb(WatchListOpenHelper openHelper, String originalTitle) {
        mDbHelper = openHelper;
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ListManager lm = getInstance();
//        int index = lm.mAnimeMap.get(originalTitle);
//        lm.mAnimeMap.remove(originalTitle);

        String selection = AnimeInfoEntry.COLUMN_ANIME_TITLE + " = ?";
        String[] selectionArgs = new String[] {originalTitle};

        db.delete(AnimeInfoEntry.TABLE_NAME, selection, selectionArgs);
    }

    public boolean contains(String animeTitle) {
        return mAnimeSet.contains(animeTitle);
    }

//    no clue if this works, but still need to work on UI first
    public static void updateDb(WatchListOpenHelper openHelper, String originalTitle,
                               String title, String rating, String isSketch) {
        mDbHelper = openHelper;
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, title);
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, rating);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, isSketch);

        ListManager lm = ListManager.getInstance();
//        int index = lm.mAnimeMap.get(originalTitle);
//        lm.mAnimeMap.remove(originalTitle);
//        lm.mAnimeMap.put(title, index);

        String selection = AnimeInfoEntry.COLUMN_ANIME_TITLE + " = ?";
        String[] selectionArgs = new String[] {originalTitle};

        db.update(AnimeInfoEntry.TABLE_NAME, values, selection, selectionArgs);

//        need to return index
//        return lm.mAnimeMap.get(title);
    }

    public static int insertInDb(AnimeRecyclerAdapter animeRecyclerAdapter, WatchListOpenHelper dbOpenHelper,
                                 String title, String rating, String isSketch) {
        mDbHelper = dbOpenHelper;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, title);
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, rating);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, isSketch);

        return (int) db.insertOrThrow(AnimeInfoEntry.TABLE_NAME, null, values) - 1;
    }

    public List<Anime> sort(List<Anime> arr) {
        if(arr.size() <= 1) {
            return  arr;
        }

        List<Anime> l1 = copy(arr, 0, arr.size() / 2);
        List<Anime> l2 = copy(arr, arr.size() / 2, arr.size());

        l1 = sort(l1);
        l2 = sort(l2);

        return combine(l1, l2);
    }

    private List<Anime> combine(List<Anime> left, List<Anime> right) {
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

    private List<Anime> copy(List<Anime> list, int start, int end) {
        if(start < 0 || start > list.size() || end > list.size()) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        ArrayList<Anime> r = new ArrayList<>();
        for(int i = start; i < end; i++) {
            r.add(list.get(i));
        }
        return r;
    }

    public static WatchListOpenHelper getDbHelper() {
        return mDbHelper;
    }
}
