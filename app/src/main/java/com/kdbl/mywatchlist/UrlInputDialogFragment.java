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

public class UrlInputDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inputDialogView = inflater.inflate(R.layout.url_input_dialog, null);

        builder.setView(inputDialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String URL = ((EditText) inputDialogView.findViewById(R.id.editTextURL)).getText().toString();
//                        The line below is for testing purposes
                        URL = "https://myanimelist.net/anime/11757/Sword_Art_Online";
                        Bundle URLResult = new Bundle();
                        URLResult.putString("URL", URL);
                        URLDisplayDialogFragment dialogFragment = new URLDisplayDialogFragment();
                        dialogFragment.setArguments(URLResult);
                        dialogFragment.show(getParentFragmentManager(), "displayURLContent");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UrlInputDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
