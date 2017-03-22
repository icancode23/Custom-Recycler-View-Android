package com.example.nipunarora.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nipunarora on 23/03/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder> {
    private ArrayList<Movie> movieslist;

    //************************************** View Holder *************************//

    public class MovieViewHolder extends RecyclerView.ViewHolder
    {
        TextView movie_name,genre;
        public MovieViewHolder(View itemView) {
            super(itemView);
            movie_name=(TextView)itemView.findViewById(R.id.movieName);
            genre=(TextView)itemView.findViewById(R.id.genre);

        }
    }
    //********************** Constructor ********************************//


    public RecyclerViewAdapter(ArrayList<Movie> movieslist) {
        this.movieslist = movieslist;
    }

    //************************************* Other Adapter Methods ******************//


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie m=movieslist.get(position);
        holder.movie_name.setText(m.getTitle());
        holder.genre.setText(m.getGenre());
    }

    @Override
    public int getItemCount() {
        return movieslist.size();
    }

    //**************************** I/O Methods *************************//
    public void addMovie(Movie m)
    {
        movieslist.add(m);
        notifyItemInserted(movieslist.size());
    }
}
