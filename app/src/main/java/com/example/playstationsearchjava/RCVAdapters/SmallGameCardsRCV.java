package com.example.playstationsearchjava.RCVAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.playstationsearchjava.R;

import java.util.HashMap;
import java.util.List;

public class SmallGameCardsRCV extends RecyclerView.Adapter<SmallGameCardsRCV.ViewHolder> {

    Context context;

    private List<HashMap<String, String>> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public SmallGameCardsRCV(Context context, List<HashMap<String, String>> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_small_games, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> game = mData.get(position);

        String name = game.get("name");
        String image = game.get("image");

        Glide.with(this.context)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImage());

        holder.name.setText(name);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView image;
        LinearLayout item;

        ViewHolder(View itemView) {

            super(itemView);

            image = itemView.findViewById(R.id.image);
            item = itemView.findViewById(R.id.item_content);
            name = itemView.findViewById(R.id.name);

            item.setOnClickListener(this);
        }

        public ImageView getImage(){ return this.image;}

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public HashMap<String, String> getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
