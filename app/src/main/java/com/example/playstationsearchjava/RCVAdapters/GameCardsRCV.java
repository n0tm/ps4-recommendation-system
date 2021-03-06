package com.example.playstationsearchjava.RCVAdapters;

import android.content.Context;
import android.graphics.Bitmap;
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

public class GameCardsRCV extends RecyclerView.Adapter<GameCardsRCV.ViewHolder> {

    Context context;

    private List<HashMap<String, String>> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public GameCardsRCV(Context context, List<HashMap<String, String>> data) {
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

        if (desc.length() > 170) desc = desc.substring(0, 170) + "...";

        Glide.with(this.context)
                .load(mData.get(position).get("image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImage());

        holder.name.setText(name);
        holder.description.setText(desc);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView description;
        ImageView image;
        LinearLayout item;

        ViewHolder(View itemView) {

            super(itemView);

            image = itemView.findViewById(R.id.image);
            item = itemView.findViewById(R.id.item_content);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);

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

    public List<HashMap<String, String>> getmData() {
        return mData;
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
