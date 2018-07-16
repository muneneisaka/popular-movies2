package com.munenendereba.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

//Movie Adapter to load the recycler view
public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> movieData;
    private LayoutInflater mInflater;

    ReviewsRecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.movieData = data;
    }

    @Override
    public ReviewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_movie_review, parent, false);
        return new ViewHolder(view);
    }

    //binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rvReviews.setText(movieData.get(position));
    }

    //get total number of cells
    @Override
    public int getItemCount() {
        return movieData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rvReviews;

        ViewHolder(View itemView) {
            super(itemView);
            rvReviews = itemView.findViewById(R.id.textViewReviews);
            //itemView.setOnClickListener(this);
        }
    }
}
