package com.munenendereba.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Movie Adapter to load the recycler view
public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> movieData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    TrailersRecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.movieData = data;
    }

    @Override
    public TrailersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_movie_trailer, parent, false);
        return new ViewHolder(view);
    }

    //binds the videos to the video view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Uri uri = Uri.parse(movieData.get(position));
        Picasso.get()
                .load(movieData.get(position))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .resize(371, 125)
                .centerCrop()
                .into(holder.trailersImageView);
    }

    //get total number of cells
    @Override
    public int getItemCount() {
        return movieData.size();
    }

    //allows click events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    //parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView trailersImageView;

        ViewHolder(View itemView) {
            super(itemView);
            trailersImageView = itemView.findViewById(R.id.imageViewTrailers);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
