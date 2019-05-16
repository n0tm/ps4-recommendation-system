package com.example.playstationsearchjava.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private Context context;
    private FavoriteGames FavoriteGames;
    private Categories Categories;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "playstation";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED = "created";
    private static final String KEY_TRANSLATION = "translation";
    private static final String TABLE_FAVORITE_GAMES = "favoriteGames";

    private SQLiteDatabase DB;

    public Database(Context context)
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.DB = this.getReadableDatabase();
    }

    public FavoriteGames FavoriteGames() {
        if (this.FavoriteGames == null) this.FavoriteGames = new FavoriteGames(context, this.DB);

        return this.FavoriteGames;
    }

    public Categories Categories() {
        if (this.Categories == null) this.Categories = new Categories(context, this.DB);

        return this.Categories;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_ID + " TEXT, " + KEY_TRANSLATION + " TEXT, "
                + KEY_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        String CREATE_FAVORITE_GAMES_TABLE = "CREATE TABLE " + TABLE_FAVORITE_GAMES + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_FAVORITE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_GAMES);

        onCreate(db);
    }
}
