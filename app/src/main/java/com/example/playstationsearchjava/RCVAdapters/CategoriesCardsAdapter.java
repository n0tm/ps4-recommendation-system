package com.example.playstationsearchjava.RCVAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.playstationsearchjava.R;

import java.util.List;

public class CategoriesCardsAdapter extends RecyclerView.Adapter<CategoriesCardsAdapter.ViewHolder> {

private List<String> mData;
private LayoutInflater mInflater;
private ItemClickListener mClickListener;

        // data is passed into the constructor
        public CategoriesCardsAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        }

// inflates the row layout from xml when needed
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_category_card, parent, false);
        return new ViewHolder(view);
        }

// binds the data to the TextView in each row
@Override
public void onBindViewHolder(ViewHolder holder, int position) {
        String category = mData.get(position);
        holder.myTextView.setText(category);
        }

// total number of rows
@Override
public int getItemCount() {
        return mData.size();
        }


// stores and recycles views as they are scrolled off screen
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView myTextView;

    ViewHolder(View itemView) {
        super(itemView);
        myTextView = itemView.findViewById(R.id.category_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
    }
}

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(CategoriesCardsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

// parent activity will implement this method to respond to click events
public interface ItemClickListener {
    void onItemClick(View view, int position);
}
}
