package com.example.myapp.Adapters;
import com.bumptech.glide.Glide;
import com.example.myapp.R;
import com.example.myapp.Model.Movie;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.List;
import android.view.View;






public class MovieAdapter extends BaseAdapter {


    private List<Movie> Movie_Objects;
    private final Context mContext;
    private final Movie mLock = new Movie();
    private final LayoutInflater mInflater;

    public MovieAdapter(Context context, List<Movie> Objects) {

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Movie_Objects = Objects;
    }
    public void add(Movie Mobjects) {
        synchronized (mLock) {
            Movie_Objects.add(Mobjects);
        }
        notifyDataSetChanged();
    }
    public void setData(List<Movie> Movie_data) {
        clear();
        for (Movie movie : Movie_data) {
            add(movie);
        }
    }
    public Context getContext() {
        return mContext;
    }
    public void clear() {
        synchronized (mLock) {
            Movie_Objects.clear();
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return Movie_Objects.size();
    }
    @Override
    public Movie getItem(int position) {
        return Movie_Objects.get(position);
    }

    public static class ViewHolder {
        public final TextView title_movie;
        public final ImageView image_movie;

        public ViewHolder(View view) {
            title_movie = (TextView) view.findViewById(R.id.grid_item_title);
            image_movie = (ImageView) view.findViewById(R.id.grid_image);

        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder vHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.grid_movie, parent, false);
            vHolder = new ViewHolder(view);
            view.setTag(vHolder);
        }

        final Movie movie = getItem(position);
        String image_url = "http://image.tmdb.org/t/p/w185" + movie.getMovie_image();
        vHolder = (ViewHolder) view.getTag();
        Glide.with(getContext()).load(image_url).into(vHolder.image_movie);
        vHolder.title_movie.setText(movie.getMovie_title());

        return view;
    }

}
