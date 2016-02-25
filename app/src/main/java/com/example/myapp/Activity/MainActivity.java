package com.example.myapp.Activity;
import com.example.myapp.Fragment.DetailActivityFragment;
import com.example.myapp.Fragment.MainActivityFragment;
import com.example.myapp.Model.Movie;
import com.example.myapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail) == null) {
            mTwoPane = false;
        }
        else
        {
        mTwoPane = true;
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail, new DetailActivityFragment(),
                            DetailActivityFragment.TAG)
                    .commit();
        }
    }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (!mTwoPane) {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivityFragment.Movie_detail, movie);
            startActivity(intent);

        } else {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.Movie_detail, movie);
            DetailActivityFragment Movie_Fragment = new DetailActivityFragment();
            Movie_Fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail, Movie_Fragment, DetailActivityFragment.TAG)
                    .commit();
        }
    }
}
