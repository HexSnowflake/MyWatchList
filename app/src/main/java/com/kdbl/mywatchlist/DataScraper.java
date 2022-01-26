package com.kdbl.mywatchlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class DataScraper implements Executor {

    public static final int MAX_ITERATION_TO_CONSIDER = 25;
    private final String mURL;
    private Set<String> mInfoTags;

    public DataScraper(String URL) {
        mURL = URL;
        mInfoTags = new HashSet<>();
        mInfoTags.add("English");
        mInfoTags.add("Episodes");
        mInfoTags.add("Status");
        mInfoTags.add("Aired");
    }

    public void populateAnimeInfo(View urlDisplayView, boolean isNew) {
        this.execute(new Runnable() {
            @Override
            public void run() {
                try {
//                    Access airing info
                    Map<String, String> displayInfo = new HashMap<>();
                    Document document = Jsoup.connect(mURL).get();
                    Elements animeInfo = document.select("div.spaceit_pad");
                    Iterator<Element> infoIterator = animeInfo.iterator();
                    int i = 0;
                    while(infoIterator.hasNext() && i < MAX_ITERATION_TO_CONSIDER) {
                        String[] temp = infoIterator.next().text().split(":");
                        if(mInfoTags.contains(temp[0])) {
                            displayInfo.put(temp[0], temp[1]);
                        }
                        i++;
                    }
//                    Access anime name
                    Element animeTitle = document.selectFirst("h1.title-name.h1_bold_none");
//                    Populate view
                    if(isNew) {
                        urlDisplayView.findViewById(R.id.editTextTitle).post(() ->
                                ((TextView) urlDisplayView.findViewById(R.id.editTextTitle))
                                        .setText(animeTitle.text()));
                    }
                    urlDisplayView.findViewById(R.id.englishTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.englishTextView))
                                    .setText("English: " + displayInfo.get("English")));
                    urlDisplayView.findViewById(R.id.episodeTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.episodeTextView))
                                    .setText("Episodes: " + displayInfo.get("Episodes")));
                    urlDisplayView.findViewById(R.id.statusTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.statusTextView))
                                    .setText("Status: " + displayInfo.get("Status")));
                    urlDisplayView.findViewById(R.id.airingTextView).post(() ->
                            ((TextView) urlDisplayView.findViewById(R.id.airingTextView))
                                    .setText("Aired: " + displayInfo.get("Aired")));
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
