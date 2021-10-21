package com.kdbl.mywatchlist;

import android.provider.BaseColumns;

public final class AnimeDatabaseContract {
    private AnimeDatabaseContract() {} //cannot be created

    public static final class AnimeInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "anime_info";
        public static final String COLUMN_ANIME_TITLE = "anime_title";
        public static final String COLUMN_ANIME_RATING = "anime_rating";
        public static final String COLUMN_IS_SKETCH = "is_sketch";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_ANIME_TITLE + " TEXT UNIQUE NOT NULL, " +
                        COLUMN_ANIME_RATING + "INTEGER NOT NULL, " +
                        COLUMN_IS_SKETCH + " TEXT NOT NULL)";
    }
}
