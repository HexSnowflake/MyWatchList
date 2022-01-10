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
    String mDataURL;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("URLView", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        mDataURL = result.getString("URL");
                    }
                });
        mDataURL = getArguments().getString("URL");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataScraper dataScraper = new DataScraper(mDataURL);
        View urlDisplayView = inflater.inflate(R.layout.url_display_dialog, null);
        dataScraper.populateAnimeInfo(urlDisplayView);
        dataScraper.populateCoverImage(urlDisplayView);
        return urlDisplayView;
    }
}
