package com.kdbl.mywatchlist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class FileParser {

//    TODO: The recycler's displayed items must be updated once parse csv is called
    private final WatchListOpenHelper mDbOpenHelper;

    public FileParser(WatchListOpenHelper openHelper) {
        mDbOpenHelper = openHelper;
    }

    public void parseCsvFile(InputStream in) {
        Queue<Anime> parsedList = new LinkedList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            while(bufferedReader.readLine() != null) {
                String[] extractedLine = bufferedReader.readLine().split(",");
                parsedList.add(
                        new Anime(extractedLine[0],
                                Integer.parseInt(extractedLine[1]),
                                Boolean.parseBoolean(extractedLine[2]),
                                extractedLine[3]));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.w("parseCsvFile: failed to parse file", exception.toString());
            throw new java.lang.UnsupportedOperationException();
        }

//        save to db
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        for(Anime anime : parsedList) {
            ContentValues values = new ContentValues();
            values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, anime.getTitle());
            values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, anime.getRating());
            values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, anime.getIsSketch());
            if(!anime.getUrl().isEmpty()) {
                values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_URL, anime.getUrl());
            }

            db.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);
        }

    }
}
