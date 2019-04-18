package com.example.playstationsearchjava;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.example.playstationsearchjava.Database.DatabaseHandler;
import com.example.playstationsearchjava.Database.Models.FavoriteGame;
import com.example.playstationsearchjava.RCVAdapters.SmallGameCardsRCV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameCardActivity extends AppCompatActivity implements SmallGameCardsRCV.ItemClickListener {

    SmallGameCardsRCV adapter;
    ImageView image;
    TextView name;
    TextView description;
    Boolean inFavorite;
    Integer game_id;
    FloatingActionButton fab;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.game_id = Integer.parseInt(getIntent().getStringExtra("game_id"));
        this.db = new DatabaseHandler(this);

        this.image = findViewById(R.id.image);
        this.description = findViewById(R.id.description);
        this.name = findViewById(R.id.name);

        this.fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            String message;
            String color;

            if (!inFavorite) {
                color = "#D81B60";
                message = "Игра была добавлена в избранное!";
                this.inFavorite = true;
                db.addFavoriteGame(new FavoriteGame(this.game_id));
            } else {
                color = "#303F9F";
                message = "Игра была убрана из избранных...";
                this.inFavorite = false;
                db.deleteFavoriteGame(new FavoriteGame(this.game_id));
            }

            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            this.fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
//                fab.setBackgroundColor(Color.parseColor(color));
        });

        init();
    }

    private void init()
    {
        FavoriteGame game = this.db.getFavoriteGame(this.game_id);
        if (game == null) this.inFavorite = false;
        else {
            this.fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D81B60")));
            this.inFavorite = true;
        }

        String url = "https://karaganda.gamerz.kz/wp-content/uploads/edd/2017/12/dd35d676433a3a4b75aa218976cdee63_b.jpg";

        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this.image);

        this.name.setText("Horizon");

        List<HashMap<String, String>> games = new ArrayList<>();

        HashMap<String, String> tempData = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            tempData.put("name", "Horizon");
            tempData.put("image", "https://karaganda.gamerz.kz/wp-content/uploads/edd/2017/12/dd35d676433a3a4b75aa218976cdee63_b.jpg");

            games.add(tempData);
        }

        // set up the RecyclerView
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
//            intent_AllTransaction.putExtra("user_id",  getIntent().getStringExtra("user_id") );
        startActivity(intent);
    }
}
