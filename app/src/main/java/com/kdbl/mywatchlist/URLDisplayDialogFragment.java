package com.kdbl.mywatchlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

public class URLDisplayDialogFragment extends DialogFragment {
    String[] mData;
    private String mOriginalTitle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mData = (String[]) getArguments().getCharSequenceArray("urlDisplayInfo");
        DataScraper dataScraper = new DataScraper(mData[0]);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View urlDisplayView = inflater.inflate(R.layout.url_display_dialog, null);
        dataScraper.populateAnimeInfo(urlDisplayView);
        dataScraper.populateCoverImage(urlDisplayView, Integer.parseInt(mData[1]), Integer.parseInt(mData[2]));
        mOriginalTitle = ((EditText)urlDisplayView.findViewById(R.id.editTextTitle)).getText().toString();
        return new AlertDialog.Builder(requireContext())
                .setView(urlDisplayView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        save data within db
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        delete data from db
                    }
                }).create();
    }

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataScraper dataScraper = new DataScraper(mData[0]);
        View urlDisplayView = inflater.inflate(R.layout.url_display_dialog, null);
        dataScraper.populateAnimeInfo(urlDisplayView);
        dataScraper.populateCoverImage(urlDisplayView, Integer.parseInt(mData[1]), Integer.parseInt(mData[2]));
        return urlDisplayView;
    }*/
}
