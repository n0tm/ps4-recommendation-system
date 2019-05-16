package com.example.playstationsearchjava;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.playstationsearchjava.Database.Database;
import com.example.playstationsearchjava.Database.FavoriteGames;
import com.example.playstationsearchjava.Database.Models.Category;
import com.example.playstationsearchjava.Database.Models.FavoriteGame;
import com.example.playstationsearchjava.RCVAdapters.SmallGameCardsRCV;
import com.example.playstationsearchjava.Utils.Api.Models.Game;
import com.example.playstationsearchjava.Utils.Api.YoApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameCardActivity extends AppCompatActivity implements SmallGameCardsRCV.ItemClickListener {

    SmallGameCardsRCV adapter;
    ImageView image;
    TextView name;
    TextView description;
    Boolean inFavorite;
    FavoriteGame game = new FavoriteGame();
    FloatingActionButton fab;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game.setID(Integer.parseInt(getIntent().getStringExtra("game_id")));

        this.image = findViewById(R.id.image);
        this.description = findViewById(R.id.description);
        this.name = findViewById(R.id.name);

        this.fab = findViewById(R.id.fab);

        this.db = new Database(this);

        if (this.db.FavoriteGames().getById(game.getID()) == null) this.inFavorite = false;
        else {
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D81B60")));
            this.inFavorite = true;
        }

        fab.setOnClickListener(view -> {

            String message;
            String color;

            if (!inFavorite) {
                color = "#D81B60";
                message = "Игра была добавлена в избранное!";
                this.inFavorite = true;
                db.FavoriteGames().add(game);
            } else {
                color = "#303F9F";
                message = "Игра была убрана из избранных...";
                this.inFavorite = false;
                db.FavoriteGames().remove(this.game);
            }

            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            this.fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        });

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
                List<Game> Game = (List<com.example.playstationsearchjava.Utils.Api.Models.Game>) bundle.getSerializable("Game");
                List<Game> similarGames = (List<com.example.playstationsearchjava.Utils.Api.Models.Game>) bundle.getSerializable("similar games");
                initGame(Game.get(0), similarGames);
                return;
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

            SharedPreferences settings = getSharedPreferences("Auth", 0);
            String Cookie = settings.getString("Cookie", null);

            List<Game> currentGame = api.Games().getById(Cookie, game.getID());
            List<Game> similarGames = api.Games().getTop(Cookie, 10, currentGame.get(0).getGenres());


            bundle.putSerializable("Game", (Serializable) currentGame);
            bundle.putSerializable("similar games", (Serializable) similarGames);
            bundle.putInt("action", 1);
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };

    private void initGame(Game game, List<Game> similarGames)
    {

        Glide.with(this)
                .load(game.getImageLink())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this.image);



        this.name.setText(game.getName());
        this.description.setText(game.getSummary());



        List<HashMap<String, String>> games = new ArrayList<>();


        for (int i = 0; i < similarGames.size(); i++) {
            HashMap<String, String> tempData = new HashMap<>();
            Game currentGame = similarGames.get(i);

            tempData.put("name", currentGame.getName());
            tempData.put("image", currentGame.getImageLink());
            tempData.put("id", String.valueOf(currentGame.getGameId()));

            games.add(tempData);
        }

        RecyclerView recyclerView = findViewById(R.id.similar_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new SmallGameCardsRCV(this, games);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(GameCardActivity.this, GameCardActivity.class);
        intent.putExtra("game_id",  game.getID());
        startActivity(intent);
    }
}
