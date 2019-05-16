package com.example.playstationsearchjava.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.playstationsearchjava.Database.Models.Category;
import com.example.playstationsearchjava.Database.Models.FavoriteGame;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Categories {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "playstation";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED = "created";
    private static final String KEY_TRANSLATION = "translation";

    private SQLiteDatabase DB;

    public Categories(Context context, SQLiteDatabase DB) {
        this.DB = DB;
    }

    public void add(Category category) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, category.getID());

        DB.insert(TABLE_CATEGORIES, null, values);
    }

    public Category getById(int id) {
        Cursor cursor = DB.query(TABLE_CATEGORIES, new String[] { KEY_ID,
                        KEY_CREATED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }
        if (!cursor.moveToFirst()) return null;

        Category category = new Category();

        category.setID(cursor.getString(0));
        category.setTranslation(cursor.getString(1));
        category.setCreated(Timestamp.valueOf(cursor.getString(2)));


        return category;
    }

    public List<Category> getAll() {
        List<Category> categoriesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

        Cursor cursor = DB.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();

                category.setID(cursor.getString(0));
                category.setTranslation(cursor.getString(1));
                category.setCreated(Timestamp.valueOf(cursor.getString(2)));

                categoriesList.add(category);
            } while (cursor.moveToNext());
        }


        return categoriesList;
    }

    public void remove(Category category) {
        DB.delete(TABLE_CATEGORIES, KEY_ID + " = ?", new String[] { String.valueOf(category.getID()) });
    }

    public void deleteAll() {
        DB.delete(TABLE_CATEGORIES, null, null);
    }

}
