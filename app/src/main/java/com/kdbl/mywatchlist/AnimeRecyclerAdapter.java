package com.kdbl.mywatchlist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

public class AnimeRecyclerAdapter extends RecyclerView.Adapter<AnimeRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private Cursor mCursor;
    private final LayoutInflater mLayoutInflater;
    private int mAnimeTitlePos;
    private int mAnimeRatingPos;
    private int mIdPos;

    public AnimeRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
//        used to create views
        mLayoutInflater = LayoutInflater.from(mContext);
//        used to iterate through database
        mCursor = cursor;
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if(mCursor == null)
            return;
//        get index from mCursor
        mAnimeTitlePos = mCursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_TITLE);
        mAnimeRatingPos = mCursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_RATING);
        mIdPos = mCursor.getColumnIndex(AnimeInfoEntry._ID);
    }

    public void changeCursor(Cursor cursor) {
        if(mCursor != null)
            mCursor.close();
        mCursor = cursor;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        the view created using layout inflater is pointed towards parent as its root
        View itemView = mLayoutInflater.inflate(R.layout.anime_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String animeTitle = mCursor.getString(mAnimeTitlePos);
        String animeRating = mCursor.getString(mAnimeRatingPos);
        int id = mCursor.getInt(mIdPos);

        holder.mTextTitle.setText(animeTitle);
        holder.mTextRating.setText(animeRating);
        holder.mID = id;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextTitle;
        public final TextView mTextRating;
        public int mID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextRating = (TextView) itemView.findViewById(R.id.text_rating);
        }
    }
}
