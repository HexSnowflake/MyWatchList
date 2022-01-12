package com.kdbl.mywatchlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
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
            /*
            DialogHelper dialogHelper = new DialogHelper(this, mAnimeRecyclerAdapter,
                    mDbOpenHelper, -1);
            dialogHelper.generateDialog(R.layout.new_anime_dialog, null);*/
            NewAnimeQueryDialogFragment dialogFragment = new NewAnimeQueryDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "newAnime");
        });

        getSupportFragmentManager().setFragmentResultListener("urlToDisplay", this, (requestKey, result) -> {
            String[] urlDisplayInfo = new String[3];
            String url = result.getString("url");
            urlDisplayInfo[0] = url;
            urlDisplayInfo[1] = String.valueOf(getWindowManager().getCurrentWindowMetrics().getBounds().width());
            urlDisplayInfo[2] = String.valueOf(getWindowManager().getCurrentWindowMetrics().getBounds().height());
            Bundle bundle = new Bundle();
            bundle.putCharSequenceArray("urlDisplayInfo", urlDisplayInfo);
            URLDisplayDialogFragment displayDialogFragment = new URLDisplayDialogFragment();
            displayDialogFragment.setArguments(bundle);
            displayDialogFragment.show(getSupportFragmentManager(), "displayUrlFragment");
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