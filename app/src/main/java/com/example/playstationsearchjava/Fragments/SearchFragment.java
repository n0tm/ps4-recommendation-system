package com.example.playstationsearchjava.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.playstationsearchjava.GameCardActivity;
import com.example.playstationsearchjava.RCVAdapters.CategoriesCardsAdapter;
import com.example.playstationsearchjava.RCVAdapters.GameCardsRCV;
import com.example.playstationsearchjava.R;
import com.example.playstationsearchjava.RCVAdapters.SearchGamesRCV;
import com.example.playstationsearchjava.Utils.Api.Models.Game;
import com.example.playstationsearchjava.Utils.Api.YoApi;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.Nullable;

public class SearchFragment extends Fragment implements SearchGamesRCV.ItemClickListener {

    SearchGamesRCV adapter;
    private String GameText;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        EditText input = getView().findViewById(R.id.searchInput);

        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                GameText = s.toString();

                Thread thread = new Thread(loadGameThread);
                thread.start();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    final
    Handler handler = new Handler() {

        private final Integer DRAW_GAMES = 1;

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Integer action = bundle.getInt("action");
            if (action == DRAW_GAMES) {
                List<Game> Games = (List<com.example.playstationsearchjava.Utils.Api.Models.Game>) bundle.getSerializable("Games");
                init(Games);
            }
        }
    };

    Runnable loadGameThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            YoApi api = new YoApi();
            Message handlerMessage = handler.obtainMessage();
            Bundle bundle = new Bundle();

            SharedPreferences settings = getContext().getSharedPreferences("Auth", 0);
            String Cookie = settings.getString("Cookie", null);

            List<Game> gamesList = api.Games().search(Cookie, GameText);

            bundle.putSerializable("Games", (Serializable) gamesList);
            bundle.putInt("action", 1);
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };

    private void init(List<Game> games)
    {
        RecyclerView recyclerView = getView().findViewById(R.id.games);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new SearchGamesRCV(this.getContext(), games);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this.getContext(), GameCardActivity.class);
        intent.putExtra("game_id",  adapter.getItem(position).getGameId().toString());
        startActivity(intent);
    }

}
