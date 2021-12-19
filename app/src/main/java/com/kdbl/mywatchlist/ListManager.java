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
}
