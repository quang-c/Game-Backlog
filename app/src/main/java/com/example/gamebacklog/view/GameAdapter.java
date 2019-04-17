package com.example.gamebacklog.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebacklog.R;
import com.example.gamebacklog.model.Game;

import java.util.List;

// It carries the data from the arrayList and delivers it to the grid cell layout file.

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mGamesList;

    public GameAdapter(List<Game> gamesList){
        this.mGamesList = gamesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.game_grid_cell, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GameAdapter.ViewHolder viewHolder, int i) {
        final Game game = mGamesList.get(i);
        viewHolder.title.setText(game.getTitle());
        viewHolder.platform.setText(game.getPlatform());
        viewHolder.status.setText(game.getStatus());
        viewHolder.date.setText(game.getDate());
    }

    @Override
    public int getItemCount() {
        return mGamesList.size();
    }

    public void updateGamesList(List<Game> newGamesList) {
        mGamesList = newGamesList;
        if (newGamesList != null)
            this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView platform;
        private TextView status;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            platform = itemView.findViewById(R.id.cardPlatform);
            status = itemView.findViewById(R.id.cardStatus);
            date = itemView.findViewById(R.id.cardDate);
        }
    }
}
