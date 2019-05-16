package com.example.playstationsearchjava.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.playstationsearchjava.Database.Database;
import com.example.playstationsearchjava.Database.Models.FavoriteGame;
import com.example.playstationsearchjava.GameCardActivity;
import com.example.playstationsearchjava.RCVAdapters.GameCardsRCV;
import com.example.playstationsearchjava.R;
import com.example.playstationsearchjava.Utils.Api.Models.Game;
import com.example.playstationsearchjava.Utils.Api.YoApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.Nullable;


public class FavoriteGamesFragment extends Fragment implements GameCardsRCV.ItemClickListener {

    GameCardsRCV adapter;
    private Database db;
    private List<FavoriteGame> games;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_games, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.db = new Database(this.getContext());

        this.games = this.db
                .FavoriteGames()
                .getAll();

        this.db.close();

        Thread thread = new Thread(loadGameThread);
        thread.start();
    }

    @SuppressLint("HandlerLeak")
    final
    Handler handler = new Handler() {

        private final Integer DRAW_GAME = 1;

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Integer action = bundle.getInt("action");
            if (action == DRAW_GAME) {
                List<Game> Games = (List<com.example.playstationsearchjava.Utils.Api.Models.Game>) bundle.getSerializable("Game");
                initGame(Games);
            }
        }
    };

    Runnable loadGameThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            YoApi api = new YoApi();
            List<Game> gamesList = new ArrayList<>();
            Message handlerMessage = handler.obtainMessage();
            Bundle bundle = new Bundle();

            SharedPreferences settings = getContext().getSharedPreferences("Auth", 0);
            String Cookie = settings.getString("Cookie", null);

            for (int i = 0; i < games.size(); i++) {
                gamesList.add(api.Games().getById(Cookie, games.get(i).getID()).get(0));
            }


            bundle.putSerializable("Game", (Serializable) gamesList);
            bundle.putInt("action", 1);
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };

    private void initGame(List<Game> games)
    {
        List<HashMap<String, String>> rcvHashMap = new ArrayList<>();

        for (int i = 0; i < games.size(); i++) {
            HashMap<String, String> tempData = new HashMap<>();

            Game gameData = games.get(i);


            tempData.put("name", gameData.getName());
            tempData.put("id", String.valueOf(gameData.getGameId()));
            tempData.put("description", gameData.getSummary());
            tempData.put("image", gameData.getImageLink());

            rcvHashMap.add(tempData);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.gamesRCV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new GameCardsRCV(this.getContext(), rcvHashMap);
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
