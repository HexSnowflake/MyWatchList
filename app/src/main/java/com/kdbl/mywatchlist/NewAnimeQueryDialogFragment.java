package com.kdbl.mywatchlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NewAnimeQueryDialogFragment extends DialogFragment {
    public static final String NEW_ANIME_QUERY_DIALOG_FRAGMENT_TAG = "newAnimeQuery";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Anime").setItems(new String[]{"URL", "Manual input"}, (dialog, which) -> {
            switch (which) {
                case 0 :
                    DialogFragment dialogFragment = new UrlInputDialogFragment();
                    dialogFragment.show(getParentFragmentManager(), UrlInputDialogFragment.URL_INPUT_DIALOG_FRAGMENT_TAG);
                    break;
                default:
                    DialogFragment manualInputDF = new ManualInputDialogFragment();
                    manualInputDF.show(getParentFragmentManager(), ManualInputDialogFragment.MANUAL_INPUT_DIALOG_FRAGMENT_TAG);
                    break;
            }
        });
        return builder.create();
    }
}
