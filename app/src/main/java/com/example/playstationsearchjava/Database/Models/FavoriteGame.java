package com.example.playstationsearchjava.Database.Models;

import java.sql.Timestamp;

public class FavoriteGame {
    int _id;
    Timestamp _created;

    public FavoriteGame() { }

    public FavoriteGame(int id) {
        this._id = id;
    }

    public FavoriteGame(int id, Timestamp created) {
        this._id = id;
        this._created = created;
    }

    public Timestamp getCreated() {
        return _created;
    }

    public void setCreated(Timestamp created) {
        this._created = created;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

}
