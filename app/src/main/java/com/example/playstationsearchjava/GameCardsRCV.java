package com.example.playstationsearchjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class GameCardsRCV extends RecyclerView.Adapter<GameCardsRCV.ViewHolder> {

    Context context;

    private List<HashMap<String, String>> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    GameCardsRCV(Context context, List<HashMap<String, String>> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_games, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> game = mData.get(position);

        String name = game.get("name");
        String desc = game.get("description");


//        URL url = null;
//
//        try {
//            url = new URL(game.get("image"));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        Bitmap imageBitmap = null;
//
//        try {
//            imageBitmap = BitmapFactory.decodeStream(url.openConnection() .getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Glide.with(this.context)
                .load(mData.get(position).get("image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImage());
//        holder.image.setImageBitmap(imageBitmap);
        holder.name.setText(name);
        holder.description.setText(desc);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView description;
        ImageView image;

        ViewHolder(View itemView) {

            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);
        }

        public ImageView getImage(){ return this.image;}

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    HashMap<String, String> getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
