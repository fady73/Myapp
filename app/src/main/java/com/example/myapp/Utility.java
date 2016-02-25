package com.example.myapp;
import com.example.myapp.Data.MovieContract;
import android.content.Context;
import android.database.Cursor;


public class Utility {

    public static int isFavorited(Context context, int movie_id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[] { Integer.toString(movie_id) },
                null
        );
        int fmovie = cursor.getCount();
        cursor.close();
        return fmovie;
    }

    public static String buildImageUrl(int width, String fileName) {
        return "http://image.tmdb.org/t/p/w" + Integer.toString(width) + fileName;
    }
}
