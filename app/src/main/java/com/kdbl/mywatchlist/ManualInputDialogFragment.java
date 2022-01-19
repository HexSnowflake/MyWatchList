package com.kdbl.mywatchlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.zip.Inflater;

public class ManualInputDialogFragment extends DialogFragment {

    public static final String TAG = "Manual Input";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        return new AlertDialog.Builder(requireContext())
                .setView(inflater.inflate(R.layout.new_anime_dialog, null))
                .setTitle("New Anime")
                .setPositiveButton("Save", (dialog, which) -> {
//                        saves new anime
                }).create();
    }
}
