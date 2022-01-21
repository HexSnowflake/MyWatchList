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
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = ((EditText) inputDialogView.findViewById(R.id.editTextURL)).getText().toString();
                        Bundle urlResult = new Bundle();
                        urlResult.putString("url", url);
                        getParentFragmentManager().setFragmentResult("urlToDisplay", urlResult);
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
