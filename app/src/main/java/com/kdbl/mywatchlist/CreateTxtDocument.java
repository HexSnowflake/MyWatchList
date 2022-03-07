package com.kdbl.mywatchlist;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

public class CreateTxtDocument extends ActivityResultContracts.CreateDocument {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull String input) {
        super.createIntent(context, input);
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, input);
        return intent;
    }
}
