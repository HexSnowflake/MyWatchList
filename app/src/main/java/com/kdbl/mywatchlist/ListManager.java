package com.kdbl.mywatchlist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

import java.util.ArrayList;
import java.util.List;

public class ListManager
{
    private static ListManager instance = null;
    private List<Anime> mAnimeList = new ArrayList<>();

//    make singleton
    public static ListManager getInstance() {
        if(instance == null) {
            instance = new ListManager();
        }
        return instance;
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

//    load data from database
    public static void loadDatabase(WatchListOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] animeListColumns = {
                AnimeInfoEntry.COLUMN_ANIME_TITLE,
                AnimeInfoEntry.COLUMN_ANIME_RATING,
                AnimeInfoEntry.COLUMN_IS_SKETCH};
        String orderBy = AnimeInfoEntry.COLUMN_ANIME_RATING + " DESC"
                + "," + AnimeInfoEntry.COLUMN_ANIME_TITLE;
        final Cursor animeCursor = db.query(AnimeInfoEntry.TABLE_NAME, animeListColumns,
                null, null, null, null, orderBy);
        loadAnimeFromDatabase(animeCursor);
    }

    private static void loadAnimeFromDatabase(Cursor cursor) {
        int animeTitlePos = cursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_TITLE);
        int animeRatingPos = cursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_RATING);
        int animeIsSketchPos = cursor.getColumnIndex(AnimeInfoEntry.COLUMN_IS_SKETCH);

        ListManager lm = getInstance();
        lm.mAnimeList.clear();
        while(cursor.moveToNext()) {
            String animeTitle = cursor.getString(animeTitlePos);
            int animeRating = cursor.getInt(animeRatingPos);
            String animeIsSketch = cursor.getString(animeIsSketchPos);

            Anime anime = new Anime(animeTitle, animeRating, animeIsSketch.equals("true"));
            lm.mAnimeList.add(anime);
        }

        cursor.close();
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

    public List<Anime> getAnimeList() {
        return mAnimeList;
    }
}
