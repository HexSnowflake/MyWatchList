package com.kdbl.mywatchlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataScraper implements Executor {

    public static final int NUM_DATA = 8;
    private final String mURL;

    public DataScraper(String URL) {
        mURL = "https://myanimelist.net/anime/11757/Sword_Art_Online";
    }

    public void populateAnimeInfo(View urlDisplayView, boolean isNew) {
        this.execute(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[NUM_DATA];
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
//                    Populate view
                    if(isNew) {
                        urlDisplayView.findViewById(R.id.editTextTitle).post(() ->
                                ((TextView) urlDisplayView.findViewById(R.id.editTextTitle))
                                        .setText(data[0]));
                    }
                    urlDisplayView.findViewById(R.id.englishTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.englishTextView))
                                    .setText(data[2]));
                    urlDisplayView.findViewById(R.id.episodeTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.episodeTextView))
                                    .setText(data[4]));
                    urlDisplayView.findViewById(R.id.statusTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.statusTextView))
                                    .setText(data[5]));
                    urlDisplayView.findViewById(R.id.airingTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.airingTextView))
                                    .setText(data[6]));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Log.w("populateAnimeInfo", exception.toString());
                    throw new java.lang.UnsupportedOperationException();
                }
            }
        });
    }

    public void populateCoverImage(View urlDisplayView) {
        populateCoverImage(urlDisplayView, 0, 0);
    }

    public void populateCoverImage(View urlDisplayView, int screenWidth, int screenHeight) {
        this.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(mURL).get();
                    Element animeImageName = document.selectFirst("h1.title-name.h1_bold_none");
                    Element imageURL = document.selectFirst("img[alt*=" + animeImageName.text() + "]");
                    InputStream inputStream = (InputStream) new URL(imageURL.absUrl("data-src")).getContent();
                    final Bitmap scaledBitmap = ImageEditor.resize(BitmapFactory.decodeStream(inputStream), screenWidth, screenHeight);
                    urlDisplayView.findViewById(R.id.coverImageView).post(() ->
                            ((ImageView) urlDisplayView.findViewById(R.id.coverImageView))
                                    .setImageBitmap(scaledBitmap));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Log.w("populateCoverImage", exception.toString());
                    throw new java.lang.UnsupportedOperationException();
                }
            }
        });
    }

    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
