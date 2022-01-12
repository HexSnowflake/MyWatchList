package com.kdbl.mywatchlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

public class URLDisplayDialogFragment extends DialogFragment {
    String[] mData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = (String[]) getArguments().getCharSequenceArray("urlDisplayInfo");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataScraper dataScraper = new DataScraper(mData[0]);
        View urlDisplayView = inflater.inflate(R.layout.url_display_dialog, null);
        dataScraper.populateAnimeInfo(urlDisplayView);
        dataScraper.populateCoverImage(urlDisplayView, Integer.parseInt(mData[1]), Integer.parseInt(mData[2]));
        return urlDisplayView;
    }
}
