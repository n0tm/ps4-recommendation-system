package com.example.playstationsearchjava.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.playstationsearchjava.Database.Models.FavoriteGame;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FavoriteGames {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "playstation";
    private static final String TABLE_FAVORITE_GAMES = "favoriteGames";
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED = "created";

    private SQLiteDatabase DB;

    public FavoriteGames(Context context, SQLiteDatabase DB) {
        this.DB = DB;
    }

    public void add(FavoriteGame game) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, game.getID());

        this.DB.insert(TABLE_FAVORITE_GAMES, null, values);
    }

    public FavoriteGame getById(int id) {
        Cursor cursor = this.DB.query(TABLE_FAVORITE_GAMES, new String[] { KEY_ID,
                        KEY_CREATED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }
        if (!cursor.moveToFirst()) return null;

        FavoriteGame game = new FavoriteGame();

        game.setID(Integer.parseInt(cursor.getString(0)));
        game.setCreated(Timestamp.valueOf(cursor.getString(1)));

        return game;
    }

    public List<FavoriteGame> getAll() {
        List<FavoriteGame> gamesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE_GAMES;

        Cursor cursor = this.DB.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                FavoriteGame game = new FavoriteGame();
                game.setID(Integer.parseInt(cursor.getString(0)));
                game.setCreated(Timestamp.valueOf(cursor.getString(1)));
                gamesList.add(game);

            } while (cursor.moveToNext());
        }

        return gamesList;
    }

    public void remove(FavoriteGame game) {
        this.DB.delete(TABLE_FAVORITE_GAMES, KEY_ID + " = ?", new String[] { String.valueOf(game.getID()) });
    }

    public void deleteAll() {
        this.DB.delete(TABLE_FAVORITE_GAMES, null, null);
    }

}
