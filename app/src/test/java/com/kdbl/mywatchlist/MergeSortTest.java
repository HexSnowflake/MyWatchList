package com.kdbl.mywatchlist;

import static org.junit.Assert.assertEquals;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class MergeSortTest {

    @Test
    public void alphabeticalSort() {
        ListManager tester = new ListManager();

        ArrayList<Anime> testArr = new ArrayList<>();
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
}