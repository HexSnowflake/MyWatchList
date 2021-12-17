package com.kdbl.mywatchlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

// note it is bad practice to have any class as static or singleton because of memory leaks
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
            DialogHelper dialogHelper = new DialogHelper(this, mAnimeRecyclerAdapter,
                    mDbOpenHelper, -1);
            dialogHelper.addButton(null, false);
        });

        initializeDisplayContent();
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
        super.onResume();
    }

    private void initializeDisplayContent() {
        mRecyclerView = findViewById(R.id.list_anime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAnimeRecyclerAdapter = new AnimeRecyclerAdapter(this, mDbOpenHelper, null);
        mRecyclerView.setAdapter(mAnimeRecyclerAdapter);
    }
}