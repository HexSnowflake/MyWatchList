package com.kdbl.mywatchlist;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kdbl.mywatchlist.AnimeDatabaseContract.AnimeInfoEntry;

public class AnimeRecyclerAdapter extends RecyclerView.Adapter<AnimeRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final WatchListOpenHelper mDbOpenHelper;

    private Cursor mCursor;
    private int mColTitlePos;
    private int mColRatingPos;
    private int mColIsSketchPos;
    private int mColIDPos;

    public AnimeRecyclerAdapter(Context context, WatchListOpenHelper openHelper, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mDbOpenHelper = openHelper;
        populateColumnPos();
//        used to create views
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    private void populateColumnPos() {
        if(mCursor == null)
            return;
//        get col index from cursor
        mColTitlePos = mCursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_TITLE);
        mColRatingPos = mCursor.getColumnIndex(AnimeInfoEntry.COLUMN_ANIME_RATING);
        mColIsSketchPos = mCursor.getColumnIndexOrThrow(AnimeInfoEntry.COLUMN_IS_SKETCH);
        mColIDPos = mCursor.getColumnIndexOrThrow(AnimeInfoEntry._ID);
    }

    public void changeCursorAndUpdateData(Cursor cursor) {
        changeCursor(cursor);
        populateColumnPos();
        notifyDataSetChanged();
    }

    public void changeCursor(Cursor cursor) {
        if(mCursor != null) {
            mCursor.close();
        }
        mCursor = cursor;
    }

    public void notifyDatabaseChanged(WatchListOpenHelper openHelper) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String[] animeListColumns = {
                AnimeInfoEntry.COLUMN_ANIME_TITLE,
                AnimeInfoEntry.COLUMN_ANIME_RATING,
                AnimeInfoEntry.COLUMN_IS_SKETCH,
                AnimeInfoEntry._ID};
        String orderBy = AnimeInfoEntry.COLUMN_ANIME_RATING + " DESC"
                + "," + AnimeInfoEntry.COLUMN_ANIME_TITLE;
        final Cursor animeCursor = db.query(AnimeInfoEntry.TABLE_NAME, animeListColumns,
                null, null, null, null, orderBy);
        changeCursorAndUpdateData(animeCursor);
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
//        Associate data with view at the specific position
        mCursor.moveToPosition(position);
        String title = mCursor.getString(mColTitlePos);
        String rating = "" + mCursor.getString(mColRatingPos);
        int id = mCursor.getInt(mColIDPos);
        holder.mTextTitle.setText(title);
//        Note setText only takes string. if it's int, it'll treat it as resource id
        holder.mTextRating.setText(rating);
        holder.mId = id;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextTitle;
        public final TextView mTextRating;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextRating = itemView.findViewById(R.id.text_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper dialogHelper = new DialogHelper(mContext, AnimeRecyclerAdapter.this,
                            mDbOpenHelper, ViewHolder.this.getBindingAdapterPosition());
                    Dialog dialog = dialogHelper.generateDialog(R.layout.update_anime_dialog);
                    EditText title = dialog.findViewById(R.id.update_anime_title);
                    EditText rating = dialog.findViewById(R.id.update_anime_rating);
                    EditText isSketch = dialog.findViewById(R.id.update_anime_isSketch);

                    title.setText(mTextTitle.getText().toString());
                    rating.setText(mTextRating.getText().toString());

                    mCursor.moveToPosition(ViewHolder.this.getBindingAdapterPosition());
                    dialogHelper.addButton(mCursor, false);
                    dialogHelper.addButton(mCursor, true);
                }
            });
        }
    }
}
