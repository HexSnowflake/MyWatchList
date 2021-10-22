package com.kdbl.mywatchlist;

import static com.kdbl.mywatchlist.AnimeDatabaseContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WatchListOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyWatchList.db";
    public static final int DATABASE_VERSION = 1;

    public WatchListOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AnimeInfoEntry.SQL_CREATE_TABLE);

//        for testing
        ContentValues values = new ContentValues();
        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, "re:zero");
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, 6);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, "eromanga sensei");
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, 7);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, true);

        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, "senryuu girl");
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, 8);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, "k-on");
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, 7);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, "gj bu");
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, 5);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, "netoge no yome");
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, 8);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, "chunnibyou");
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, 9);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        long rowID = sqLiteDatabase.insert(AnimeInfoEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
