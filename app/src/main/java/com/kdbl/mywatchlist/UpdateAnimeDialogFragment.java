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

public class UpdateAnimeDialogFragment extends DialogFragment {
    public static final String TAG = "Update Anime";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String[] displayData = (String[]) getArguments().getCharSequenceArray("NonUrlDisplayData");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View updateAnimeDialogView = inflater.inflate(R.layout.update_anime_dialog, null);
        ((EditText)(updateAnimeDialogView.findViewById(R.id.update_anime_title))).setText(displayData[0]);
        ((EditText)(updateAnimeDialogView.findViewById(R.id.update_anime_rating))).setText(displayData[1]);
        ((EditText)(updateAnimeDialogView.findViewById(R.id.update_anime_isSketch))).setText(displayData[2]);
        return new AlertDialog.Builder(requireContext())
                .setView(updateAnimeDialogView)
                .setTitle("Update Anime")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        save data into data base
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        delete data from data base
                    }
                }).create();
    }
}
