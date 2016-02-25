package com.example.myapp.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bumptech.glide.Glide;
import com.example.myapp.R;
import com.example.myapp.Utility;
import com.example.myapp.Adapters.ReviewAdapter;
import com.example.myapp.Adapters.TrailerAdapter;
import com.example.myapp.Data.MovieContract;
import com.example.myapp.Model.Movie;
import com.example.myapp.Model.Review;
import com.example.myapp.Model.Trailer;
import com.linearlistview.LinearListView;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class DetailActivityFragment extends Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    public static final String Movie_detail = "DETAIL_MOVIE";
    private ImageView Movie_view;
    private Movie movies;
    private LinearListView trailers_movie;
    private TrailerAdapter trailer_adapter;
    private CardView trailers_card;
    private LinearListView reviews_movie;
    private ReviewAdapter review_adapter;
    private CardView reviews_card;
    private TextView voteaverage_movie;
    private TextView overview_movie;
    private TextView tiltle_movie;
    private TextView date_movie;
    private Toast massage;
    private ScrollView detail_movie;
    private ShareActionProvider movie_share;
    private Trailer trailer_modal;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (movies != null) {
            inflater.inflate(R.menu.menu_fragment_detail, menu);
            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
            MenuItem action_share = menu.findItem(R.id.action_share);
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected void onPostExecute(Integer inFavorited) {
                    action_favorite.setIcon(inFavorited != 1 ?
                            R.drawable.abc_btn_rating_star_off_mtrl_alpha
                            : R.drawable.abc_btn_rating_star_on_mtrl_alpha );
                }
                protected Integer doInBackground(Void... params) {
                    return Utility.isFavorited(getActivity(), movies.getMovie_id());
                }
            }.execute();
            movie_share = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);
            if (trailer_modal != null) {
                movie_share.setShareIntent(createShareMovieIntent());
            }
        }
    }
    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movies.getMovie_title() + " " +
                "http://www.youtube.com/watch?v=" + trailer_modal.getMovie_key());
        return shareIntent;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id_favorite = item.getItemId();
        switch (id_favorite) {
            case R.id.action_favorite:
                if (movies != null) {

                    new AsyncTask<Void, Void, Integer>() {

                        @Override
                        protected Integer doInBackground(Void... params) {
                            return Utility.isFavorited(getActivity(), movies.getMovie_id());
                        }

                        @Override
                        protected void onPostExecute(Integer inFavorited) {
                            if (inFavorited != 1) {
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues movie_values = new ContentValues();

                                        movie_values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movies.getMovie_id());
                                        movie_values.put(MovieContract.MovieEntry.COLUMN_TITLE, movies.getMovie_title());
                                        movie_values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movies.getMovie_overview());
                                        movie_values.put(MovieContract.MovieEntry.COLUMN_IMAGE, movies.getMovie_image());
                                        movie_values.put(MovieContract.MovieEntry.COLUMN_IMAGE2, movies.getMovie_image2());
                                        movie_values.put(MovieContract.MovieEntry.COLUMN_RATING, movies.getMovie_rating());
                                        movie_values.put(MovieContract.MovieEntry.COLUMN_DATE, movies.getMovie_date());

                                        return getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                                                movie_values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if (massage != null) {
                                            massage.cancel();
                                        }
                                        massage = Toast.makeText(getActivity(), getString(R.string.Added_favorites), Toast.LENGTH_SHORT);
                                        massage.show();
                                    }
                                }.execute();
                            }
                            else {
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(
                                                MovieContract.MovieEntry.CONTENT_URI,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                                new String[]{Integer.toString(movies.getMovie_id())}
                                        );
                                    }

                                    @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if (massage != null) {
                                            massage.cancel();
                                        }
                                        massage = Toast.makeText(getActivity(), getString(R.string.Removed_favorites), Toast.LENGTH_SHORT);
                                        massage.show();
                                    }
                                }.execute();

                            }
                        }
                    }.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            movies = arguments.getParcelable(DetailActivityFragment.Movie_detail);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        detail_movie = (ScrollView) rootView.findViewById(R.id.detail_layout);

        if (movies != null) {
            detail_movie.setVisibility(View.VISIBLE);
        } else {
            detail_movie.setVisibility(View.INVISIBLE);
        }

        Movie_view = (ImageView) rootView.findViewById(R.id.detail_image);

        tiltle_movie = (TextView) rootView.findViewById(R.id.detail_title);
        overview_movie = (TextView) rootView.findViewById(R.id.detail_overview);
        date_movie = (TextView) rootView.findViewById(R.id.detail_date);
        voteaverage_movie = (TextView) rootView.findViewById(R.id.detail_vote_average);

        trailers_movie = (LinearListView) rootView.findViewById(R.id.detail_trailers);
        reviews_movie = (LinearListView) rootView.findViewById(R.id.detail_reviews);

        reviews_card = (CardView) rootView.findViewById(R.id.detail_reviews_cardview);
        trailers_card = (CardView) rootView.findViewById(R.id.detail_trailers_cardview);

        trailer_adapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        trailers_movie.setAdapter(trailer_adapter);

        trailers_movie.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {
                Trailer trailer = trailer_adapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getMovie_key()));
                startActivity(intent);
            }
        });

        review_adapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
        reviews_movie.setAdapter(review_adapter);

        if (movies != null) {

            String image_url = Utility.buildImageUrl(185, movies.getMovie_image2());

            Glide.with(this).load(image_url).into(Movie_view);

            tiltle_movie.setText(movies.getMovie_title());
            overview_movie.setText(movies.getMovie_overview());

            String movie_date = movies.getMovie_date();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                date_movie.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            voteaverage_movie.setText(Integer.toString(movies.getMovie_rating()));
        }

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (movies != null) {
            new FetchReviewsTask().execute(Integer.toString(movies.getMovie_id()));
            new FetchTrailersTask().execute(Integer.toString(movies.getMovie_id()));

        }
    }
    public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
        private List<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailer> tra_result = new ArrayList<>();

            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer fet_trailer = new Trailer(trailer);
                    tra_result.add(fet_trailer);
                }
            }

            return tra_result;
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.api_key))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    trailers_card.setVisibility(View.VISIBLE);
                    if (trailer_adapter != null) {
                        trailer_adapter.clear();
                        for (Trailer mov_trailer : trailers) {
                            trailer_adapter.add(mov_trailer);
                        }
                    }

                    trailer_modal = trailers.get(0);
                    if (movie_share != null) {
                        movie_share.setShareIntent(createShareMovieIntent());
                    }
                }
            }
        }
    }
    public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private List<Review> getReviewsDataFromJson(String jsonStr) throws JSONException {
            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            List<Review> revi_result = new ArrayList<>();

            for(int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);
                revi_result.add(new Review(review));
            }

            return revi_result;
        }

        @Override
        protected List<Review> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.api_key))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getReviewsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null) {
                if (reviews.size() > 0) {
                    reviews_card.setVisibility(View.VISIBLE);
                    if (review_adapter != null) {
                        review_adapter.clear();
                        for (Review mo_review : reviews) {
                            review_adapter.add(mo_review);
                        }
                    }
                }
            }
        }
    }

}
