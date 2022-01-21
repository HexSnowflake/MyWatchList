package com.kdbl.mywatchlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
        View view = inflater.inflate(R.layout.new_anime_dialog, null);
        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle("New Anime")
                .setPositiveButton("Save", (dialog, which) -> {
                    Bundle results = new Bundle();
                    String[] inputs = new String[3];
                    inputs[0] = ((EditText) (view.findViewById(R.id.update_anime_title))).getText().toString();
                    inputs[1] = ((EditText) (view.findViewById(R.id.update_anime_rating))).getText().toString();
                    inputs[2] = ((EditText) (view.findViewById(R.id.update_anime_isSketch))).getText().toString();
                    results.putCharSequenceArray("ManualInputNewAnime", inputs);
                    getParentFragmentManager().setFragmentResult("ManualInputNewAnime", results);
                }).create();
    }
}
