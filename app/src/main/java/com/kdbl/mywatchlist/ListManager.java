package com.kdbl.mywatchlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ListManager
{
    private static ListManager instance = null;
    private List<Anime> mAnimeList = new ArrayList<>();
    private Set<String> mAnimeSet = new HashSet<>();

    //    make singleton
    public static ListManager getInstance() {
        if(instance == null) {
            instance = new ListManager();
        }
        return instance;
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
            lm.mAnimeSet.add(anime.getTitle());
        }

        cursor.close();
    }

    public boolean contains(String animeTitle) {
        return mAnimeSet.contains(animeTitle);
    }

    public static int insertInDb(AnimeRecyclerAdapter animeRecyclerAdapter, WatchListOpenHelper dbOpenHelper,
                                 String title, String rating, String isSketch) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(AnimeInfoEntry.COLUMN_ANIME_TITLE, title);
        values.put(AnimeInfoEntry.COLUMN_ANIME_RATING, rating);
        values.put(AnimeInfoEntry.COLUMN_IS_SKETCH, isSketch);

        db.insert(AnimeInfoEntry.TABLE_NAME, null, values);

        return getInstance().insert(animeRecyclerAdapter, new Anime(title, Integer.parseInt(rating),
                isSketch.equalsIgnoreCase("yes")
                        || isSketch.equalsIgnoreCase("y")
                        || isSketch.equalsIgnoreCase("true")));
    }

    private int insert(AnimeRecyclerAdapter animeRecyclerAdapter, Anime anime) {
        int min = 0;
        int max = mAnimeList.size() - 1;
        int mid = (mAnimeList.size() - 1) / 2;

//        implement binary search
        while(min < max) {
            if(anime.compareTo(mAnimeList.get(mid)) < 0) {
                min = mid + 1;
            } else {
                max = mid;
            }
            mid = (max - min) / 2 + min;
        }

        List<Anime> nAnimeList = new ArrayList<>();
        for(int i = 0; i < mAnimeList.size(); i++) {
            if(i == mid) {
                nAnimeList.add(anime);
            }
            nAnimeList.add(mAnimeList.get(i));
        }
        mAnimeList = nAnimeList;
        mAnimeSet.add(anime.getTitle());
        animeRecyclerAdapter.setAnimes(nAnimeList);

        return mid;
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

    public List<Anime> getAnimeList() {
        return mAnimeList;
    }
}
