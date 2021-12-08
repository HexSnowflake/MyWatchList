package com.kdbl.mywatchlist;

import static com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry.*;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.LayoutRes;

import com.google.android.material.snackbar.Snackbar;

public class DialogHelper {
    private final Context mContext;
    private AnimeRecyclerAdapter mAnimeRecyclerAdapter;
    private WatchListOpenHelper mWatchListOpenHelper;

    public DialogHelper(Context context, AnimeRecyclerAdapter adapter, WatchListOpenHelper openHelper) {
        mContext = context;
        mAnimeRecyclerAdapter = adapter;
        mWatchListOpenHelper = openHelper;
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

    public void addButton(Cursor cursor, boolean isDelete) {
        Dialog dialog;
        Button button;
        if(cursor == null) {
            dialog = generateDialog(R.layout.new_anime_dialog);
        } else {
            dialog = generateDialog(R.layout.update_anime_dialog);
        }
        if(isDelete) {
            button = dialog.findViewById(R.id.delete_button);
        } else {
            button = dialog.findViewById(R.id.save_button);
        }

        button.setOnClickListener(v -> {
            String nAnimeTitle = ((EditText)(dialog.findViewById(R.id.update_anime_title)))
                    .getText().toString();
            String nAnimeRating = ((EditText)(dialog.findViewById(R.id.update_anime_rating)))
                    .getText().toString();
            String nAnimeIsSketch = ((EditText)(dialog.findViewById(R.id.update_anime_isSketch)))
                    .getText().toString();

            if(isDelete) {
                deleteAnime(mAnimeRecyclerAdapter, mWatchListOpenHelper, cursor);
            }
            else if(cursor == null)
                insertNewAnime(v, mAnimeRecyclerAdapter, mWatchListOpenHelper, nAnimeTitle, nAnimeRating, nAnimeIsSketch);
            else {
                updateAnime(mAnimeRecyclerAdapter, mWatchListOpenHelper, cursor,
                        nAnimeTitle, nAnimeRating, nAnimeIsSketch);
            }

            dialog.dismiss();
        });
    }


//    TODO: A new cursor probably needs to be returned since the old cursor given will become outdated
//    performance for that may still be an issue
    private void deleteAnime(AnimeRecyclerAdapter adapter, WatchListOpenHelper openHelper, Cursor cursor) {
        String originalTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANIME_TITLE));
        ListManager.deleteFromDb(openHelper, originalTitle);
        adapter.notifyItemRemoved(cursor.getColumnIndexOrThrow(_ID));
    }

    private void updateAnime(AnimeRecyclerAdapter adapter, WatchListOpenHelper openHelper,
                             Cursor cursor, String title, String rating, String isSketch) {
        String originalTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANIME_TITLE));
        ListManager.updateDb(openHelper, originalTitle, title, rating, isSketch);
        adapter.notifyItemChanged(cursor.getColumnIndexOrThrow(_ID));
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
