package com.kdbl.mywatchlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WatchListOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerView;
    private AnimeRecyclerAdapter mAnimeRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbOpenHelper = new WatchListOpenHelper(this);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Snackbar.make(v, "clicked fab", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            onButtonShowDialogueClick();
        });

        initializeDisplayContent();
    }



    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    private void initializeDisplayContent() {
        ListManager.loadDatabase(mDbOpenHelper);

        mRecyclerView = findViewById(R.id.list_anime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        List<Anime> animeList = ListManager.getInstance().getAnimeList();
        mAnimeRecyclerAdapter = new AnimeRecyclerAdapter(this, animeList);
        mRecyclerView.setAdapter(mAnimeRecyclerAdapter);
    }

    public void onButtonShowDialogueClick() {
        final Dialog dialog = new Dialog(this);
//        title present in custom layout, disable the default title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        set dialog default window to be transparent
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        allow disable dialog by clicking outside the dialog
        dialog.setCancelable(true);
//        associate layout with dialog
        dialog.setContentView(R.layout.new_anime_popup);



        Button saveButton = dialog.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                String nAnimeTitle = ((EditText)(dialog.findViewById(R.id.new_anime_title)))
                        .getText().toString();
                String nAnimeRating = ((EditText)(dialog.findViewById(R.id.new_anime_rating)))
                        .getText().toString();
                String nAnimeIsSketch = ((EditText)(dialog.findViewById(R.id.new_anime_isSketch)))
                        .getText().toString();

                if(ListManager.getInstance().contains(nAnimeTitle)) {
                    Snackbar.make(v, "already contains this anime", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                mAnimeRecyclerAdapter.notifyItemInserted(
                        ListManager.insertInDb(mAnimeRecyclerAdapter ,mDbOpenHelper,
                                nAnimeTitle, nAnimeRating, nAnimeIsSketch)
                );

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}