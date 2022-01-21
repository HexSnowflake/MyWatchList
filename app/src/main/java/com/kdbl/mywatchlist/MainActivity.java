package com.kdbl.mywatchlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

        getSupportFragmentManager().setFragmentResultListener("ManualInputNewAnime", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String[] inputData = (String[]) result.getCharSequenceArray("ManualInputNewAnime");
                ListManager.insertInDb(mDbOpenHelper, inputData[0], inputData[1], inputData[2]);
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
        });

        getSupportFragmentManager().setFragmentResultListener("ManualInputUpdateAnime", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String[] inputData = (String[]) result.getCharSequenceArray("ManualInputUpdateAnime");
                ListManager.updateDb(mDbOpenHelper,inputData[3], inputData[0], inputData[1], inputData[2]);
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
        });

        getSupportFragmentManager().setFragmentResultListener("ManualInputDeleteAnime", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                ListManager.deleteFromDb(mDbOpenHelper, result.getString("ManualInputDeleteAnime"));
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
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

        mAnimeRecyclerAdapter = new AnimeRecyclerAdapter(this, mDbOpenHelper, null, this);
        mRecyclerView.setAdapter(mAnimeRecyclerAdapter);
    }
}