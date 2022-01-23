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
    private String isNew;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        TODO: Implement support for user to update title themselves
        mData = (String[]) getArguments().getCharSequenceArray("urlDisplayInfo");
        isNew = getArguments().getString("isNew");
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
                        Bundle result = new Bundle();
                        String[] inputs = new String[4];
                        inputs[0] = ((EditText) urlDisplayView.findViewById(R.id.editTextTitle)).getText().toString();
                        inputs[1] = ((EditText) urlDisplayView.findViewById(R.id.editTextRating)).getText().toString();
                        inputs[2] = ((EditText) urlDisplayView.findViewById(R.id.editTextIsSketch)).getText().toString();
                        inputs[3] = mOriginalTitle;
                        result.putCharSequenceArray("UrlInputData", inputs);
                        result.putString("UrlInputUrl", mData[0]);
                        if(isNew != null) {
                            result.putString("isNew", isNew);
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
