package com.example.myapp.Model;

import org.json.JSONException;
import org.json.JSONObject;


public class Review {

    private String movie_id;
    private String movie_author;
    private String movie_content;
    public Review(){}

    public String getMovie_id() { return movie_id; }

    public String getMovie_author() { return movie_author; }

    public String getMovie_content() { return movie_content; }

    public Review(JSONObject trailer) throws JSONException {
        this.movie_id = trailer.getString("id");
        this.movie_author = trailer.getString("author");
        this.movie_content = trailer.getString("content");
    }


}
