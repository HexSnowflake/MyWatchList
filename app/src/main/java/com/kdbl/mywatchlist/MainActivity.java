package com.kdbl.mywatchlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WatchListOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbOpenHelper = new WatchListOpenHelper(this);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "clicked fab", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                onButtonShowPopupClick(v);
            }
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
        AnimeRecyclerAdapter animeRecyclerAdapter = new AnimeRecyclerAdapter(this, animeList);
        mRecyclerView.setAdapter(animeRecyclerAdapter);
    }

    public void onButtonShowPopupClick(View view) {
//        inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View newAnimePopup = inflater.inflate(R.layout.new_anime_popup, null);

//        create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow animePopup = new PopupWindow(newAnimePopup, width, height, focusable);

//        show the popup window
        animePopup.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button saveButton = newAnimePopup.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "clicked save", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        animePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                animePopup.dismiss();
            }
        });
    }
}