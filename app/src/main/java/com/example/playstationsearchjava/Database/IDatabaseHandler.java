package com.example.playstationsearchjava.Database;

import com.example.playstationsearchjava.Database.Models.FavoriteGame;

import java.util.List;

public interface IDatabaseHandler {
    public void addFavoriteGame(FavoriteGame game);
    public FavoriteGame getFavoriteGame(int id);
    public List<FavoriteGame> getAllFavoriteGames();
    public void deleteFavoriteGame(FavoriteGame game);
    public void deleteAll();
}
