package com.kdbl.mywatchlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class ListManager
{
    private static ListManager instance = null;
//    private List<Anime> mAnimeList = new ArrayList<>();
//    private Map<String, Integer> mAnimeMap = new HashMap<>();
//    TODO: The set still needs to be updated when things are changed
//    TODO: have to change cursor because adapter gets its info from cursor

    private static Set<String> mSet = new HashSet<>();
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

        mSet.remove(originalTitle);

        String selection = AnimeInfoEntry.COLUMN_ANIME_TITLE + " = ?";
        String[] selectionArgs = new String[] {originalTitle};

        db.delete(AnimeInfoEntry.TABLE_NAME, selection, selectionArgs);
    }

    public boolean contains(String animeTitle) {
        return mSet.contains(animeTitle);
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

        instance.mSet.remove(originalTitle);
        instance.mSet.add(title);

        String selection = AnimeInfoEntry.COLUMN_ANIME_TITLE + " = ?";
        String[] selectionArgs = new String[] {originalTitle};

        db.update(AnimeInfoEntry.TABLE_NAME, values, selection, selectionArgs);

//        need to return index
//        return lm.mAnimeMap.get(title);
    }

    public static void insertInDb(AnimeRecyclerAdapter animeRecyclerAdapter, WatchListOpenHelper dbOpenHelper,
                                 String title, String rating, String isSketch) {
        mDbHelper = dbOpenHelper;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        instance.mSet.add(title);

        ContentValues values = new ContentValues();
        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, title);
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, rating);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, isSketch);

        db.insert(AnimeInfoEntry.TABLE_NAME, null, values);
    }
}
