package com.example.myapp.Activity;
import com.example.myapp.Fragment.DetailActivityFragment;
import com.example.myapp.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.Movie_detail,
                    getIntent().getParcelableExtra(DetailActivityFragment.Movie_detail));

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail, fragment)
                    .commit();
        }
    }
}
