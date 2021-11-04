package com.kdbl.mywatchlist;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnimeRecyclerAdapter extends RecyclerView.Adapter<AnimeRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final WatchListOpenHelper mDbOpenHelper;
    private List<Anime> mAnimes;

    public AnimeRecyclerAdapter(Context context, WatchListOpenHelper openHelper, List<Anime> animes) {
        mContext = context;
        mAnimes = animes;
        mDbOpenHelper = openHelper;
//        used to create views
        mLayoutInflater = LayoutInflater.from(mContext);
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
        Anime anime = mAnimes.get(position);
        holder.mTextTitle.setText(anime.getTitle());
//        Note setText only takes string. if it's int, it'll treat it as resource id
        holder.mTextRating.setText(String.valueOf(anime.getRating()));
    }

    @Override
    public int getItemCount() {
        return mAnimes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextTitle;
        public final TextView mTextRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextRating = itemView.findViewById(R.id.text_rating);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper dialogHelper = new DialogHelper(mContext);
                    Dialog dialog = dialogHelper.generateDialog(R.layout.update_anime_dialog);
                    EditText title = dialog.findViewById(R.id.update_anime_title);
                    EditText rating = dialog.findViewById(R.id.update_anime_rating);
                    EditText isSketch = dialog.findViewById(R.id.update_anime_isSketch);

                    title.setText(mTextTitle.getText().toString());
                    rating.setText(mTextRating.getText().toString());

                    dialogHelper.addButton(AnimeRecyclerAdapter.this, mDbOpenHelper,
                            dialog, mTextTitle.getText().toString(), false);
                    dialogHelper.addButton(AnimeRecyclerAdapter.this, mDbOpenHelper,
                            dialog, mTextTitle.getText().toString(), true);
                }
            });
        }
    }

    public void setAnimes(List<Anime> animes) {
        mAnimes = animes;
    }
}
