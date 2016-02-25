package com.example.myapp.Model;

import org.json.JSONException;
import org.json.JSONObject;



public class Trailer {

    private String movie_id;
    private String  movie_key;
    private String movie_name;
    private String movie_site;
    private String  movie_type;
    public Trailer(){}

    public String getMovie_id() {
        return movie_id;
    }

    public String getMovie_key() { return movie_key; }

    public String getMovie_name() { return movie_name; }

    public String getMovie_site() { return movie_site; }

    public String getMovie_type() { return movie_type; }

    public Trailer(JSONObject trailer) throws JSONException {
        this.movie_id = trailer.getString("id");
        this.movie_key = trailer.getString("key");
        this.movie_name = trailer.getString("name");
        this.movie_site = trailer.getString("site");
        this.movie_type = trailer.getString("type");
    }


}
