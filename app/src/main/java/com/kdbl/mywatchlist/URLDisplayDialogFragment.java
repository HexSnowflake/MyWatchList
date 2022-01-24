package com.kdbl.mywatchlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class URLDisplayDialogFragment extends DialogFragment {
    String[] mDisplayInfo;
    String[] mDisplayData;
    private String mOriginalTitle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        TODO: Implement support for user to update title themselves
        mDisplayInfo = (String[]) getArguments().getCharSequenceArray("UrlDisplayInfo");
        mDisplayData = (String[]) getArguments().getCharSequenceArray("UrlDisplayData");
        DataScraper dataScraper = new DataScraper(mDisplayInfo[0]);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View urlDisplayView = inflater.inflate(R.layout.url_display_dialog, null);
        if(mDisplayData == null) {
            dataScraper.populateAnimeInfo(urlDisplayView, true);
        } else {
            dataScraper.populateAnimeInfo(urlDisplayView, false);
            ((EditText)urlDisplayView.findViewById(R.id.editTextTitle)).setText(mDisplayData[0]);
            ((EditText)urlDisplayView.findViewById(R.id.editTextRating)).setText(mDisplayData[1]);
            ((EditText)urlDisplayView.findViewById(R.id.editTextIsSketch)).setText(mDisplayData[2]);
        }
        dataScraper.populateCoverImage(urlDisplayView, Integer.parseInt(mDisplayInfo[1]), Integer.parseInt(mDisplayInfo[2]));
        mOriginalTitle = ((EditText)urlDisplayView.findViewById(R.id.editTextTitle)).getText().toString();
        return new AlertDialog.Builder(requireContext())
                .setView(urlDisplayView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle result = new Bundle();
                        String[] inputs = new String[4];
                        inputs[0] = ((EditText) urlDisplayView.findViewById(R.id.editTextTitle)).getText().toString();
                        inputs[1] = ((EditText) urlDisplayView.findViewById(R.id.editTextRating)).getText().toString();
                        inputs[2] = ((EditText) urlDisplayView.findViewById(R.id.editTextIsSketch)).getText().toString();
                        inputs[3] = mOriginalTitle;
                        result.putCharSequenceArray("UrlInputData", inputs);
                        result.putString("UrlInputUrl", mDisplayInfo[0]);
                        if(mDisplayData != null) {
                            result.putBoolean("isNew", false);
                        } else {
                            result.putBoolean("isNew", true);
                        }
                        getParentFragmentManager().setFragmentResult("UrlInputUpdateAnime", result);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle result = new Bundle();
                        result.putString("UrlInputDeleteAnime", mOriginalTitle);
                        getParentFragmentManager().setFragmentResult("UrlInputDeleteAnime", result);
                    }
                }).create();
    }
}
