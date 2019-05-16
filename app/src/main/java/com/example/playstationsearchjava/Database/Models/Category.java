package com.example.playstationsearchjava.Database.Models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;

public class Category {

    String _id;
    String _translation;
    Timestamp _created;

    public String getTranslation() { return this._translation; }
    public void setTranslation(String Translation) { this._translation = Translation; }


    public String getID() {
        return this._id;
    }
    public void setID(String ID) { this._id = ID; }


    public Timestamp getCreated() { return this._created; }
    public void setCreated(Timestamp created) { this._created = created; }

}
