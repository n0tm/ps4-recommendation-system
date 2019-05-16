package com.example.playstationsearchjava.Utils.Api.Methods;

import com.example.playstationsearchjava.Utils.Api.Models.Game;
import com.example.playstationsearchjava.Utils.Api.Models.ResponseModel;
import com.example.playstationsearchjava.Utils.Api.YoApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Games {

    private YoApi api;

    public Games()
    {
        this.api = new YoApi();
    }

    public List<Game> getTop(String Cookie, Integer limit, String genres)
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(YoApi.API_URL + MethodsList.GET_TOP_GAMES).newBuilder();

        urlBuilder.addQueryParameter("games_limit", limit.toString());
        urlBuilder.addQueryParameter("games_genre", genres);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", Cookie)
                .build();

        ResponseModel Response = this.api.request(request);

        try {
            JSONArray Games = Response.getFullResponse().getJSONArray("Result");
            return Games.isNull(0) ? new ArrayList<>() : format(Games);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public List<Game> search(String Cookie, String game)
    {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(YoApi.API_URL + MethodsList.SEARCH_GAMES).newBuilder();

        urlBuilder.addQueryParameter("game_name", game);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", Cookie)
                .build();

        ResponseModel Response = this.api.request(request);

        try {
            JSONArray Games = Response.getFullResponse().getJSONArray("Result");
            return format(Games);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public List<Game> getById(String Cookie, Integer game_id)
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(YoApi.API_URL + MethodsList.GET_GAME_BY_ID).newBuilder();

        urlBuilder.addQueryParameter("game_id", game_id.toString());

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", Cookie)
                .build();

        ResponseModel Response = this.api.request(request);

        try {
            JSONArray Games = Response.getFullResponse().getJSONArray("Result");
            return Games.isNull(0) ? new ArrayList<>() : format(Games);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Game> format(JSONArray Games)
    {
        List<Game> result = new ArrayList<>();
        JSONObject gameObject;

        for (int i=0; i < Games.length(); i++) {

            try {
                gameObject = Games.getJSONObject(i);

                Game game = new Game();

                game.setDeveloper(gameObject.getString("Developer"));
                game.setGameId(gameObject.getInt("GameId"));
                game.setGenres(gameObject.getString("Genres"));
                game.setImageLink(gameObject.getString("ImgLink"));
                game.setMetascore(gameObject.getInt("Metascore"));
                game.setName(gameObject.getString("Name"));
                game.setOfPlayers(gameObject.getInt("OfPlayers"));
                game.setProcessedName(gameObject.getString("ProcessedName"));
                game.setSummary(gameObject.getString("Summary"));
                game.setRating(gameObject.getString("Rating"));
                game.setUsersScore(gameObject.getDouble("UsersScore"));

                result.add(game);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return result;
    }
}
