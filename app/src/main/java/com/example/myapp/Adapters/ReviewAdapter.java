package com.example.myapp.Adapters;
import com.example.myapp.Model.Review;
import com.example.myapp.R;
import java.util.List;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class ReviewAdapter extends BaseAdapter {

    private List<Review> Movie_Objects;
    private final Context  Movie_Context;
    private final Review Movie_Lock = new Review();
    private final LayoutInflater Movie_Inflater;



    public ReviewAdapter(Context context, List<Review> objects) {
        Movie_Context = context;
        Movie_Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Movie_Objects = objects;
    }
    public void add(Review Mobject) {
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
    public Review getItem(int position) {
        return Movie_Objects.get(position);
    }
    public static class ViewHolder {
        public final TextView authorView;
        public final TextView contentView;

        public ViewHolder(View view) {
            authorView = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
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
            view = Movie_Inflater.inflate(R.layout.movie_review, parent, false);
            vHolder = new ViewHolder(view);
            view.setTag(vHolder);
        }

        final Review review_movie = getItem(position);
        vHolder = (ViewHolder) view.getTag();
        vHolder.authorView.setText(review_movie.getMovie_author());
        vHolder.contentView.setText(Html.fromHtml(review_movie.getMovie_content()));

        return view;
    }



}
