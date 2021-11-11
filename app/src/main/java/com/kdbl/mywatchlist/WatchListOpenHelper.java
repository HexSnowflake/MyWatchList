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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
