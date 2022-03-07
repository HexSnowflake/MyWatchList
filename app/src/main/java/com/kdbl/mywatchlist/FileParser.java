package com.kdbl.mywatchlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
            String cur = bufferedReader.readLine();
            while(cur != null) {
                String[] extractedLine = cur.split(",");
                parsedList.add(
                        new Anime(extractedLine[0],
                                Integer.parseInt(extractedLine[1]),
                                Boolean.parseBoolean(extractedLine[2]),
                                extractedLine.length == 4 ? extractedLine[3] : null));
                cur = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.w("parseCsvFile: failed to parse file", exception.toString());
            throw new java.lang.UnsupportedOperationException();
        }

//        save to db
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        for(Anime anime : parsedList) {
            ContentValues values = new ContentValues();
            values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, anime.getTitle());
            values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, anime.getRating());
            values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, anime.getIsSketch());
            if(!anime.getUrl().isEmpty()) {
                values.put(AnimeInfoEntry.COLUMN_ANIME_URL, anime.getUrl());
            }

            db.insert(AnimeInfoEntry.TABLE_NAME, null, values);
        }
    }

    public List<Anime> convertDbToList() {
        List<Anime> animeList = new ArrayList<>();
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        String[] animeListCol = {
                AnimeInfoEntry.COLUMN_ANIME_TITLE,
                AnimeInfoEntry.COLUMN_ANIME_RATING,
                AnimeInfoEntry.COLUMN_IS_SKETCH,
                AnimeInfoEntry.COLUMN_ANIME_URL};
        Cursor cursor = db.query(AnimeInfoEntry.TABLE_NAME, animeListCol,
                null, null, null, null, null);
        int titleColPos = cursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_TITLE);
        int ratingColPos = cursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_RATING);
        int isSketchColPos = cursor.getColumnIndex(AnimeInfoEntry.COLUMN_IS_SKETCH);
        int urlColPos = cursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_URL);
        while(cursor.moveToNext()) {
            animeList.add(new Anime(
                    cursor.getString(titleColPos),
                    Integer.parseInt(cursor.getString(ratingColPos)),
                    Boolean.parseBoolean(cursor.getString(isSketchColPos)),
                    cursor.getString(urlColPos)));
        }
        cursor.close();
        return animeList;
    }
}
