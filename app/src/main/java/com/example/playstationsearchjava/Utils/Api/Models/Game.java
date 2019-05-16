package com.example.playstationsearchjava.Utils.Api.Models;

import android.annotation.SuppressLint;
import android.util.Log;

public class Game {

    private Integer GameId;
    private String Genres;
    private String Rating;
    private String Developer;
    private Integer OfPlayers;
    private String Name;
    private String ImageLink;
    private String Summary;
    private Integer Metascore;
    private Double UsersScore;
    private String ProcessedName;

    public Double getUsersScore() {
        return this.UsersScore;
    }

    public Integer getGameId() {
        return this.GameId;
    }

    public String getDeveloper() {
        return this.Developer;
    }

    public Integer getMetascore() {
        return this.Metascore;
    }

    public Integer getOfPlayers() {
        return this.OfPlayers;
    }

    public String getGenres() {
        return this.Genres;
    }

    public String getImageLink() {
        return this.ImageLink;
    }

    public String getName() {
        return this.Name;
    }

    public String getRating() {
        return this.Rating;
    }

    public String getProcessedName() {
        return this.ProcessedName;
    }

    public String getSummary() {
        return this.Summary;
    }

    public void setDeveloper(String developer) {
        this.Developer = developer;
    }

    public void setGameId(Integer gameId) {
        this.GameId = gameId;
    }

    public void setGenres(String genres) {
        this.Genres = genres;
    }

    public void setImageLink(String imageLink) {
        this.ImageLink = imageLink;
    }

    public void setRating(String rating) {
        this.Rating = rating;
    }

    public void setMetascore(Integer metascore) {
        this.Metascore = metascore;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setOfPlayers(Integer ofPlayers) {
        this.OfPlayers = ofPlayers;
    }

    public void setProcessedName(String processedName) {
        this.ProcessedName = processedName;
    }

    public void setSummary(String summary) {
        this.Summary = summary;
    }

    public void setUsersScore(double usersScore) {
        this.UsersScore = usersScore;
    }


    @SuppressLint("DefaultLocale")
    public void debug()
    {
        System.out.println(
            String.format(
            "+-+-+-+-+-+-+-+-+ [GAME INFO] +-+-+-+-+-+-+-+-+\n\n" +
            "Name - %s\n" +
            "Developer - %s\n" +
            "GameId - %d\n" +
            "Genres - %s\n" +
            "ImageLink - %s\n" +
            "Metascore - %d\n" +
            "OfPlayers - %d\n" +
            "ProcessedName - %s\n" +
            "Rating - %s\n" +
            "Summary - %s\n" +
            "UsersScore - %f\n\n" +
            "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+",

            this.Name,
            this.Developer,
            this.GameId,
            this.Genres,
            this.ImageLink,
            this.Metascore,
            this.OfPlayers,
            this.ProcessedName,
            this.Rating,
            this.Summary,
            this.UsersScore
        ));
    }

}
