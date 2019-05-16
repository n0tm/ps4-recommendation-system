package com.example.playstationsearchjava.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.playstationsearchjava.Database.Database;
import com.example.playstationsearchjava.Database.Models.Category;
import com.example.playstationsearchjava.GameCardActivity;
import com.example.playstationsearchjava.RCVAdapters.GameCardsRCV;
import com.example.playstationsearchjava.R;
import com.example.playstationsearchjava.Utils.Api.Models.Game;
import com.example.playstationsearchjava.Utils.Api.Models.ResponseModel;
import com.example.playstationsearchjava.Utils.Api.YoApi;

import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
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

        Thread thread = new Thread(loadGamesThread);
        thread.start();
    }

    private void initGames(List<Game> gamesObject)
    {
        List<HashMap<String, String>> games = new ArrayList<>();

        for (int i = 0; i < gamesObject.size(); i++) {
            HashMap<String, String> tempData = new HashMap<>();

            tempData.put("name", gamesObject.get(i).getName());
            tempData.put("id", String.valueOf(gamesObject.get(i).getGameId()));
            tempData.put("description", gamesObject.get(i).getSummary());
            tempData.put("image", gamesObject.get(i).getImageLink());
            gamesObject.get(i).debug();

            games.add(tempData);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.gamesRCV);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapter = new GameCardsRCV(this.getContext(), games);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }


    @SuppressLint("HandlerLeak")
    final
    Handler handler = new Handler() {

        private final Integer DRAW_TOP = 1;

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Integer action = bundle.getInt("action");
            if (action == DRAW_TOP) {
                List<Game> Games = (List<Game>) bundle.getSerializable("Games");
                initGames(Games);
                return;
            }
        }
    };

    Runnable loadGamesThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            YoApi api = new YoApi();
            Database db = new Database(getContext());

            Message handlerMessage = handler.obtainMessage();
            Bundle bundle = new Bundle();

            SharedPreferences settings = getContext().getSharedPreferences("Auth", 0);
            String Cookie = settings.getString("Cookie", null);

            List<Category> categories = db.Categories().getAll();

            String genres = "";

            for (int i = 0; i < categories.size(); i++) {
                if (i == categories.size() - 1) genres += String.format("%s", categories.get(i).getID());
                else genres += String.format("%s,", categories.get(i).getID());
            }

            List<Game> response = api.Games().getTop(Cookie, 20, genres);


            bundle.putSerializable("Games", (Serializable) response);
            bundle.putInt("action", 1);
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };

    @Override
    public void onItemClick(View view, int position) {

        Toast.makeText(this.getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), GameCardActivity.class);
        intent.putExtra("game_id",  adapter.getItem(position).get("id"));
        startActivity(intent);
    }

}
