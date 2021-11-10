package com.kdbl.mywatchlist;

import static org.junit.Assert.*;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.contrib.RecyclerViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.pressBack;

@RunWith(AndroidJUnit4.class)
public class AnimeCreationTest {
    static ListManager sListManager;

    @BeforeClass
    public static void classSetUp() {
        sListManager = ListManager.getInstance();
    }

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void createNewAnime() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.update_anime_title)).perform(typeText("Test 1"));
        onView(withId(R.id.update_anime_rating)).perform(typeText("7"));
        onView(withId(R.id.update_anime_isSketch)).perform(typeText("y"),
                closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
    }

    @Test
    public void selectAnime() {
        final Anime anime = sListManager.getAnimeList().
                get(sListManager.getAnimeIndex("senryuu girl"));

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