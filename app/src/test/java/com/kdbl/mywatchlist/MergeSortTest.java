package com.kdbl.mywatchlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSortTest {

    @Test
    public void alphabeticalSort() {
        ListManager tester = new ListManager();

        List<Anime> testArr = new ArrayList<>();
        testArr.add(new Anime("Apple", 9, false));
        testArr.add(new Anime("All", 9, false));
        testArr.add(new Anime("Allay", 9, false));
        testArr.add(new Anime("About", 9, false));
        testArr.add(new Anime("Cranberry", 9, false));
        testArr.add(new Anime("Zeki", 9, false));

        testArr = tester.sort(testArr);

        String result = "";
        for(int i = 0; i < testArr.size(); i++) {
            result += testArr.get(i).getTitle() + " ";
        }

        assertEquals("About All Allay Apple Cranberry Zeki ", result);
    }

    @Test
    public void ratingSort() {
        ListManager tester = new ListManager();

        List<Anime> testArray = new ArrayList<>();
        testArray.add(new Anime("Clannad", 9, false));
        testArray.add(new Anime("bouku", 5, true));
        testArray.add(new Anime("Slime", 8, false));
        testArray.add(new Anime("clock", 6, false));
        testArray.add(new Anime("girly", 7, false));


        testArray = tester.sort(testArray);

        String result = "";
        for(int i = 0; i < testArray.size(); i++) {
            result += testArray.get(i).getTitle() + " ";
        }

        assertEquals("Clannad Slime girly clock bouku ", result);
    }

    @Test
    public void edgeCaseSort() {
        ListManager tester = new ListManager();

        List<Anime> testArray = new ArrayList<>();

        List<Anime> testArray2 = new ArrayList<>();
        testArray2.add(new Anime("Violet", 9, false));

        testArray = tester.sort(testArray);
        testArray2 = tester.sort(testArray2);

        assertTrue(testArray.isEmpty());
        assertEquals("Violet", testArray2.get(0).getTitle());
    }

    @Test
    public void comprehensiveSort() {
        ListManager tester = new ListManager();

        List<Anime> testArray = new ArrayList<>();
        testArray.add(new Anime("re:zero", 6, false));
        testArray.add(new Anime("eromanga sensei", 7, true));
        testArray.add(new Anime("senryuu girl", 8, false));
        testArray.add(new Anime("k-on", 7, false));
        testArray.add(new Anime("gj bu", 5, false));
        testArray.add(new Anime("netoge no yome", 8, false));
        testArray.add(new Anime("chunnibyou", 9, false));
        testArray.add(new Anime("high school dxd", 7, true));

        testArray = tester.sort(testArray);

        String result = "";
        for(int i = 0; i < testArray.size() - 1; i++) {
            result += testArray.get(i).getTitle() + ", ";
        }
        result += testArray.get(testArray.size() - 1).getTitle();

        assertEquals("chunnibyou, netoge no yome, senryuu girl, eromanga sensei, " +
                "high school dxd, k-on, re:zero, gj bu", result);
    }
}