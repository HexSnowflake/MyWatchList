package com.kdbl.mywatchlist;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class PopulatedListTest {
    static ListManager sListManager;

    @BeforeClass
    public static void classSetUp() {
        sListManager = ListManager.getInstance();
    }

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void prepopulateDb() {
        //        for testing
        SQLiteDatabase sqLiteDatabase = ListManager.mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, "re:zero");
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, 6);
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        sqLiteDatabase.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);

        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, "eromanga sensei");
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, 7);
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, true);

        sqLiteDatabase.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);

        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, "senryuu girl");
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, 8);
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        sqLiteDatabase.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);

        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, "k-on");
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, 7);
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        sqLiteDatabase.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);

        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, "gj bu");
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, 5);
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        sqLiteDatabase.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);

        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, "netoge no yome");
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, 8);
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        sqLiteDatabase.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);

        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_TITLE, "chunnibyou");
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_ANIME_RATING, 9);
        values.put(AnimeDatabaseContract.AnimeInfoEntry.COLUMN_IS_SKETCH, false);

        sqLiteDatabase.insert(AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME, null, values);
    }

    @After
    public void clearDbA() {
        SQLiteDatabase db = ListManager.mDbHelper.getWritableDatabase();
        db.execSQL("delete from " + AnimeDatabaseContract.AnimeInfoEntry.TABLE_NAME);
    }

    @Test
    public void insertNewAnime() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.update_anime_title)).perform(typeText("Test 1"));
        onView(withId(R.id.update_anime_rating)).perform(typeText("7"));
        onView(withId(R.id.update_anime_isSketch)).perform(typeText("y"),
                closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
    }

    @Test
    public void selectAnime() {
        onView(withId(R.id.list_anime)).perform(actionOnHolderItem(withTitle("senryuu girl"), click()));
//        onData(allOf(instanceOf(Anime.class), equalTo(anime))).perform(click());
        pressBack();
    }

    //    matches ViewHolder based on title
    private static Matcher<RecyclerView.ViewHolder> withTitle(final String title) {
        return new BoundedMatcher<RecyclerView.ViewHolder, AnimeRecyclerAdapter.ViewHolder>(AnimeRecyclerAdapter.ViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("view holder with title: " + title);
            }

            @Override
            protected boolean matchesSafely(AnimeRecyclerAdapter.ViewHolder item) {
                return item.mTextTitle.getText().toString().equalsIgnoreCase(title);
            }
        };
    }
}