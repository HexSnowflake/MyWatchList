package com.kdbl.mywatchlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NewAnimeQueryDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Anime").setItems(new String[]{"URL", "Manual input"}, (dialog, which) -> {
            switch (which) {
                case 0 :
                    DialogFragment dialogFragment = new UrlInputDialogFragment();
                    dialogFragment.show(getParentFragmentManager(), "URLInput");
                    break;
                default:
                    DialogFragment manualInputDF = new ManualInputDialogFragment();
                    manualInputDF.show(getParentFragmentManager(), ManualInputDialogFragment.TAG);
                    break;
            }
        });
        return builder.create();
    }
}
