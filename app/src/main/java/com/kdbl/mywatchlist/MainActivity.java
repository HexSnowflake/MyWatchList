package com.kdbl.mywatchlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String DIALOG_INSERT = "insert";
    public static final String DIALOG_UPDATE = "update";
    private static MainActivity mInstance = null;

    private WatchListOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerView;
    private AnimeRecyclerAdapter mAnimeRecyclerAdapter;

    public static MainActivity getInstance() {
        if(mInstance == null) {
            mInstance= new MainActivity();
        }
        return  mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbOpenHelper = new WatchListOpenHelper(this);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Snackbar.make(v, "clicked fab", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            onButtonShowDialogueClick(DIALOG_INSERT);
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

    public void onButtonShowDialogueClick(String action) {
        final Dialog dialog = new Dialog(this);
//        title present in custom layout, disable the default title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        set dialog default window to be transparent
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        allow disable dialog by clicking outside the dialog
        dialog.setCancelable(true);
//        associate layout with dialog
        dialog.setContentView(R.layout.new_anime_dialog);


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

                if(action.equals(DIALOG_INSERT))
                    insertNewAnime(v, nAnimeTitle, nAnimeRating, nAnimeIsSketch);
                else if(action.equals(DIALOG_UPDATE)) {
                    updateAnime(nAnimeTitle, nAnimeRating, nAnimeIsSketch);
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateAnime(String title, String rating, String isSketch) {
        mAnimeRecyclerAdapter.notifyItemChanged(ListManager.updateDb(mDbOpenHelper ,title, rating, isSketch));
    }

    private boolean insertNewAnime(View v, String nAnimeTitle, String nAnimeRating, String nAnimeIsSketch) {
        if(ListManager.getInstance().contains(nAnimeTitle)) {
            Snackbar.make(v, "already contains this anime", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }

        mAnimeRecyclerAdapter.notifyItemInserted(
                ListManager.insertInDb(mAnimeRecyclerAdapter ,mDbOpenHelper,
                        nAnimeTitle, nAnimeRating, nAnimeIsSketch)
        );
        return false;
    }
}