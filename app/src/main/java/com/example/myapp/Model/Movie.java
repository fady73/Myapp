package com.example.myapp.Model;
import com.example.myapp.Fragment.MainActivityFragment;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {

    private int movie_id;
    private String movie_title; // original_title
    private String movie_image; // poster_path
    private String movie_image2; // backdrop_path
    private String movie_overview;
    private int movie_rating; // vote_average
    private String movie_date; // release_date

public Movie(){}
    public Movie(JSONObject movie) throws JSONException {
        this.movie_id = movie.getInt("id");
        this.movie_title = movie.getString("original_title");
        this.movie_image = movie.getString("poster_path");
        this.movie_image2 = movie.getString("backdrop_path");
        this.movie_overview = movie.getString("overview");
        this.movie_rating = movie.getInt("vote_average");
        this.movie_date = movie.getString("release_date");
    }

    public Movie(Cursor cursor) {
        this.movie_id = cursor.getInt(MainActivityFragment.COL_MOVIE_ID);
        this.movie_title = cursor.getString(MainActivityFragment.COL_TITLE);
        this.movie_image = cursor.getString(MainActivityFragment.COL_IMAGE);
        this.movie_image2 = cursor.getString(MainActivityFragment.COL_IMAGE2);
        this.movie_overview = cursor.getString(MainActivityFragment.COL_OVERVIEW);
        this.movie_rating = cursor.getInt(MainActivityFragment.COL_RATING);
        this.movie_date = cursor.getString(MainActivityFragment.COL_DATE);
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public String getMovie_image() {
        return movie_image;
    }

    public String getMovie_image2() {
        return movie_image2;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public int getMovie_rating() {
        return movie_rating;
    }

    public String getMovie_date() {
        return movie_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie[] newArray(int number) {
            return new Movie[number];
        }
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }


    };
    private Movie(Parcel in) {
        movie_id = in.readInt();
        movie_title = in.readString();
        movie_image = in.readString();
        movie_image2 = in.readString();
        movie_overview = in.readString();
        movie_rating = in.readInt();
        movie_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movie_id);
        dest.writeString(movie_title);
        dest.writeString(movie_image);
        dest.writeString(movie_image2);
        dest.writeString(movie_overview);
        dest.writeInt(movie_rating);
        dest.writeString(movie_date);
    }



}
