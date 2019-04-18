package com.example.playstationsearchjava.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.playstationsearchjava.Database.Models.FavoriteGame;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "playstation";
    private static final String TABLE_FAVORITE_GAMES = "favoriteGames";
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED = "created";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVORITE_GAMES + "("
                + KEY_ID + " INTEGER PRIMARY KEY, created DATETIME DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_GAMES);

        onCreate(db);
    }

    @Override
    public void addFavoriteGame(FavoriteGame game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, game.getID());

        db.insert(TABLE_FAVORITE_GAMES, null, values);
        db.close();
    }

    @Override
    public FavoriteGame getFavoriteGame(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITE_GAMES, new String[] { KEY_ID,
                        KEY_CREATED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }
        if (!cursor.moveToFirst()) return null;
        return new FavoriteGame(Integer.parseInt(cursor.getString(0)), Timestamp.valueOf(cursor.getString(1)));
    }

    @Override
    public List<FavoriteGame> getAllFavoriteGames() {
        List<FavoriteGame> gamesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE_GAMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FavoriteGame game = new FavoriteGame();
                game.setID(Integer.parseInt(cursor.getString(0)));
                gamesList.add(game);
            } while (cursor.moveToNext());
        }

        return gamesList;
    }

    @Override
    public void deleteFavoriteGame(FavoriteGame game) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE_GAMES, KEY_ID + " = ?", new String[] { String.valueOf(game.getID()) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE_GAMES, null, null);
        db.close();
    }

}
