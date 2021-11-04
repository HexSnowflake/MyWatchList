package com.kdbl.mywatchlist;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.LayoutRes;

import com.google.android.material.snackbar.Snackbar;

public class DialogHelper {
    private final Context mContext;

    public DialogHelper(Context context) {
        mContext = context;
    }

    public Dialog generateDialog(@LayoutRes int layoutRid) {
        final Dialog dialog = new Dialog(mContext);
//        title present in custom layout, disable the default title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        set dialog default window to be transparent
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        allow disable dialog by clicking outside the dialog
        dialog.setCancelable(true);
//        associate layout with dialog
        dialog.setContentView(layoutRid);

        dialog.show();

        return dialog;
    }

    public void addButton(AnimeRecyclerAdapter adapter, WatchListOpenHelper openHelper, Dialog dialog,
                          String originalTitle, boolean isDelete) {
        Button button;
        if(isDelete) {
            button = dialog.findViewById(R.id.delete_button);
        } else {
            button = dialog.findViewById(R.id.save_button);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nAnimeTitle = ((EditText)(dialog.findViewById(R.id.update_anime_title)))
                        .getText().toString();
                String nAnimeRating = ((EditText)(dialog.findViewById(R.id.update_anime_rating)))
                        .getText().toString();
                String nAnimeIsSketch = ((EditText)(dialog.findViewById(R.id.update_anime_isSketch)))
                        .getText().toString();

                if(isDelete) {
                    deleteAnime(adapter, openHelper, originalTitle);
                }
                else if(originalTitle == null)
                    insertNewAnime(v, adapter, openHelper, nAnimeTitle, nAnimeRating, nAnimeIsSketch);
                else {
                    updateAnime(adapter, openHelper, originalTitle, nAnimeTitle, nAnimeRating, nAnimeIsSketch);
                }

                dialog.dismiss();
            }
        });
    }

    private void deleteAnime(AnimeRecyclerAdapter adapter, WatchListOpenHelper openHelper, String originalTitle) {
        adapter.notifyItemRemoved(ListManager.deleteFromDb(openHelper, originalTitle));
    }

    //    doesn't work
    private void updateAnime(AnimeRecyclerAdapter adapter, WatchListOpenHelper openHelper,
                             String oTitle, String title, String rating, String isSketch) {
        adapter.notifyItemChanged(ListManager.updateDb(openHelper, oTitle, title, rating, isSketch));
    }

    private boolean insertNewAnime(View v, AnimeRecyclerAdapter adapter, WatchListOpenHelper openHelper,
                                   String nAnimeTitle, String nAnimeRating, String nAnimeIsSketch) {
        if(ListManager.getInstance().contains(nAnimeTitle)) {
            Snackbar.make(v, "already contains this anime", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }

        adapter.notifyItemInserted(
                ListManager.insertInDb(adapter , openHelper,
                        nAnimeTitle, nAnimeRating, nAnimeIsSketch)
        );
        return false;
    }
}
