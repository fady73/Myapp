package com.example.myapp.Adapters;
import com.bumptech.glide.Glide;
import com.example.myapp.Model.Trailer;
import java.util.List;
import com.example.myapp.R;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




public class TrailerAdapter extends BaseAdapter {


    private List<Trailer> Movie_Objects;
    private final Context  Movie_Context;
    private final Trailer Movie_Lock = new Trailer();
    private final LayoutInflater Movie_Inflater;

    public TrailerAdapter(Context context, List<Trailer> objects) {
        Movie_Context = context;
        Movie_Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Movie_Objects = objects;
    }
    public void add(Trailer Mobject) {
        synchronized (Movie_Lock) {
            Movie_Objects.add(Mobject);
        }
        notifyDataSetChanged();
    }
    public Context getContext() {
        return Movie_Context;
    }
    public void clear() {
        synchronized (Movie_Lock) {
            Movie_Objects.clear();
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return Movie_Objects.size();
    }
    @Override
    public Trailer getItem(int position) {
        return Movie_Objects.get(position);
    }
    public static class ViewHolder {
        public final ImageView image_movie;
        public final TextView name_movie;

        public ViewHolder(View view) {
            image_movie = (ImageView) view.findViewById(R.id.trailer_image);
            name_movie = (TextView) view.findViewById(R.id.trailer_name);
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
            view = Movie_Inflater.inflate(R.layout.movie_trailer, parent, false);
            vHolder = new ViewHolder(view);
            view.setTag(vHolder);
        }

        final Trailer trailer_movie = getItem(position);
        vHolder = (ViewHolder) view.getTag();
        String yt_thumbnail_url = "http://img.youtube.com/vi/" + trailer_movie.getMovie_key() + "/0.jpg";
        Glide.with(getContext()).load(yt_thumbnail_url).into(vHolder.image_movie);
        vHolder.name_movie.setText(trailer_movie.getMovie_name());

        return view;
    }


}
