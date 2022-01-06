package com.kdbl.mywatchlist;

import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

public class DataScraper {

    public static final int NUM_DATA = 8;
    private final String mURL;

    public DataScraper(String URL) {
        mURL = URL;
    }

    public String[] getAnimeInfo() {
        String[] data = new String[8];
        try {
            Document document = Jsoup.connect(mURL).get();
            Elements animeInfo = document.select("div.spaceit_pad");
            Iterator<Element> infoIterator = animeInfo.iterator();
            int i = 0;
            while(infoIterator.hasNext() && i < NUM_DATA) {
                String temp = infoIterator.next().text();
                data[i] = temp.split(":")[1].trim();
                i++;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new java.lang.UnsupportedOperationException();
        }
        return data;
    }

    public Drawable getCoverImage() {
        try {
            Document document = Jsoup.connect(mURL).get();
            Element animeImageName = document.selectFirst("h1.title-name.h1_bold_none");
            Element imageURL = document.selectFirst("img[alt*=" + animeImageName + "]");
            InputStream inputStream = (InputStream) new URL(imageURL.absUrl("data-src")).getContent();
            return Drawable.createFromStream(inputStream, animeImageName.text());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new java.lang.UnsupportedOperationException();
        }
    }
}
