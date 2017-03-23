package com.example.nipunarora.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nipunarora on 23/03/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder> {
    private ArrayList<Result> movieslist;

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


    public RecyclerViewAdapter(ArrayList<Result> movieslist) {
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
        Result m=movieslist.get(position);
        holder.movie_name.setText(m.getTitle());

    }

    @Override
    public int getItemCount() {
        return movieslist.size();
    }

    //**************************** I/O Methods *************************//
    public void addMovie(Result m)
    {
        movieslist.add(m);
        notifyItemInserted(movieslist.size());
    }
    public void clear()
    {
        movieslist.clear();
        notifyDataSetChanged();
    }
    public void refreshAdd(ArrayList<Result> d)
    {
        movieslist.addAll(d);
        notifyDataSetChanged();

    }
}
