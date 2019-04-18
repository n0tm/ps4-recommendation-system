package com.example.playstationsearchjava.Fragments;

import android.content.Intent;
import android.os.Bundle;

import com.example.playstationsearchjava.GameCardActivity;
import com.example.playstationsearchjava.RCVAdapters.GameCardsRCV;
import com.example.playstationsearchjava.R;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.Nullable;

public class TopGamesFragment extends Fragment implements GameCardsRCV.ItemClickListener {

    GameCardsRCV adapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_games, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        initGames();
    }

    private void initGames()
    {
        List<HashMap<String, String>> games = new ArrayList<>();

        for (Integer i = 0; i < 10; i++) {
            HashMap<String, String> tempData = new HashMap<>();

            tempData.put("name", "Horizon");
            tempData.put("id", String.valueOf(i));
            tempData.put("description", "Офигенная игра для ps4, подойдёт только лучшим охотникам киберпанкам.");
            tempData.put("image", "https://karaganda.gamerz.kz/wp-content/uploads/edd/2017/12/dd35d676433a3a4b75aa218976cdee63_b.jpg");

            games.add(tempData);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.gamesRCV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new GameCardsRCV(this.getContext(), games);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this.getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), GameCardActivity.class);
        intent.putExtra("game_id",  adapter.getItem(position).get("id"));
        startActivity(intent);
    }

}
