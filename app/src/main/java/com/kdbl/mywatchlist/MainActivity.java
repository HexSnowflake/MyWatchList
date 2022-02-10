package com.kdbl.mywatchlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

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
            dialogFragment.show(getSupportFragmentManager(), NewAnimeQueryDialogFragment.NEW_ANIME_QUERY_DIALOG_FRAGMENT_TAG);
        });

        setUrlInputDFListener();
        setManualNewAnimeInputDFListener();
        setManualUpdateAnimeDFListener();
        setUrlDisplayDFListener();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    private void initializeDisplayContent() {
        mRecyclerView = findViewById(R.id.list_anime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAnimeRecyclerAdapter = new AnimeRecyclerAdapter(this, mDbOpenHelper, null, this);
        mRecyclerView.setAdapter(mAnimeRecyclerAdapter);
    }

    public void createUrlDisplayDialog(String[] displayData, String url) {
        URLDisplayDialogFragment urlDisplayDF = new URLDisplayDialogFragment();
        String[] urlDisplayInfo = new String[3];
        urlDisplayInfo[0] = url;
        urlDisplayInfo[1] = String.valueOf(getWindowManager().getCurrentWindowMetrics().getBounds().width());
        urlDisplayInfo[2] = String.valueOf(getWindowManager().getCurrentWindowMetrics().getBounds().height());
        Bundle arguments = new Bundle();
        arguments.putCharSequenceArray(URLDisplayDialogFragment.DISPLAY_DATA_TAG, displayData);
        arguments.putCharSequenceArray(URLDisplayDialogFragment.DISPLAY_INFO_TAG, urlDisplayInfo);
        urlDisplayDF.setArguments(arguments);
        urlDisplayDF.show(getSupportFragmentManager(), URLDisplayDialogFragment.URL_DISPLAY_DIALOG_FRAGMENT_TAG);
    }

    public void createNonUrlDisplayDialog(String[] displayData) {
        Bundle nonUrlDisplayData = new Bundle();
        nonUrlDisplayData.putCharSequenceArray("NonUrlDisplayData", displayData);
        UpdateAnimeDialogFragment updateAnimeDF = new UpdateAnimeDialogFragment();
        updateAnimeDF.setArguments(nonUrlDisplayData);
        updateAnimeDF.show(getSupportFragmentManager(), UpdateAnimeDialogFragment.UPDATE_ANIME_DIALOG_FRAGMENT_TAG);
    }

    private void setUrlDisplayDFListener() {
        getSupportFragmentManager().setFragmentResultListener(URLDisplayDialogFragment.UPDATE_ANIME_TAG, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String[] inputs =
                        (String[]) result.getCharSequenceArray(URLDisplayDialogFragment.INPUT_DATA_TAG);
                if(ListManager.getInstance().contains(inputs[0])) {
//                    tell the user anime already exists
                }
                else if(result.getBoolean(URLDisplayDialogFragment.IS_NEW_TAG)) {
                    ListManager.insertInDB(mDbOpenHelper,
                            inputs[0], inputs[1], inputs[2],
                            result.getString(URLDisplayDialogFragment.INPUT_URL_TAG));
                } else {
                    ListManager.updateDb(mDbOpenHelper,
                            inputs[3], inputs[0], inputs[1], inputs[2],
                            result.getString(URLDisplayDialogFragment.INPUT_URL_TAG));
                }
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
        });

        getSupportFragmentManager().setFragmentResultListener(URLDisplayDialogFragment.DELETE_ANIME_TAG, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                ListManager.deleteFromDb(mDbOpenHelper,
                        result.getString(URLDisplayDialogFragment.ORIGINAL_TITLE_TAG));
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
        });
    }

    private void setManualUpdateAnimeDFListener() {
        getSupportFragmentManager().setFragmentResultListener(UpdateAnimeDialogFragment.UPDATE_ANIME_TAG, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String[] inputData = (String[]) result.getCharSequenceArray(UpdateAnimeDialogFragment.INPUT_DATA_TAG);
                ListManager.updateDb(mDbOpenHelper,inputData[3], inputData[0], inputData[1], inputData[2]);
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
        });

        getSupportFragmentManager().setFragmentResultListener(UpdateAnimeDialogFragment.DELETE_ANIME_TAG, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                ListManager.deleteFromDb(mDbOpenHelper, result.getString(UpdateAnimeDialogFragment.ORIGINAL_TITLE_TAG));
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
        });
    }

    private void setManualNewAnimeInputDFListener() {
        getSupportFragmentManager().setFragmentResultListener(ManualInputDialogFragment.NEW_ANIME_TAG, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String[] inputData = (String[]) result.getCharSequenceArray(ManualInputDialogFragment.MANUAL_INPUT_DATA_TAG);
                ListManager.insertInDb(mDbOpenHelper, inputData[0], inputData[1], inputData[2]);
                mAnimeRecyclerAdapter.notifyDatabaseChanged(mDbOpenHelper);
            }
        });
    }

    private void setUrlInputDFListener() {
        getSupportFragmentManager().setFragmentResultListener(UrlInputDialogFragment.URL_TO_DISPLAY_TAG, this, (requestKey, result) -> {
            String[] urlDisplayInfo = new String[3];
            String url = result.getString(UrlInputDialogFragment.INPUT_URL_TAG);
            urlDisplayInfo[0] = url;
            urlDisplayInfo[1] = String.valueOf(getWindowManager().getCurrentWindowMetrics().getBounds().width());
            urlDisplayInfo[2] = String.valueOf(getWindowManager().getCurrentWindowMetrics().getBounds().height());
            Bundle bundle = new Bundle();
            bundle.putCharSequenceArray(URLDisplayDialogFragment.DISPLAY_INFO_TAG, urlDisplayInfo);
            URLDisplayDialogFragment displayDialogFragment = new URLDisplayDialogFragment();
            displayDialogFragment.setArguments(bundle);
            displayDialogFragment.show(getSupportFragmentManager(), URLDisplayDialogFragment.URL_DISPLAY_DIALOG_FRAGMENT_TAG);
        });
    }
}