package com.kdbl.mywatchlist;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class FileParser {
    public void parseCsvFile(InputStream in) {
        Queue<Anime> parsedList = new LinkedList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            while(bufferedReader.readLine() != null) {
                String[] extractedLine = bufferedReader.readLine().split(",");
                parsedList.add(
                        new Anime(extractedLine[0],
                                Integer.parseInt(extractedLine[1]),
                                Boolean.parseBoolean(extractedLine[2]),
                                extractedLine[3]));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.w("parseCsvFile: failed to parse file", exception.toString());
            throw new java.lang.UnsupportedOperationException();
        }

//        save to db
    }
}
