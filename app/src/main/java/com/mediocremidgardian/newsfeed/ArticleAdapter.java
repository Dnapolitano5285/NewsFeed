package com.mediocremidgardian.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Valhalla on 9/10/16.
 */
public class ArticleAdapter extends ArrayAdapter<Article>{

    Context mContext;
    LayoutInflater inflater;
    List<Article> mArticles;
    int resourceId;

    public ArticleAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        mContext = context;
        inflater = LayoutInflater.from(getContext());
        mArticles = objects;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout listItem;

        if (convertView == null){
            listItem = (LinearLayout) inflater.inflate(resourceId, parent, false);
        } else {
            listItem = (LinearLayout)convertView;
        }

        TextView title = (TextView) listItem.findViewById(R.id.article_title);
        TextView author = (TextView) listItem.findViewById(R.id.article_author);
        TextView date = (TextView) listItem.findViewById(R.id.article_date);

        title.setText(mArticles.get(position).getTitle());
        author.setText(mArticles.get(position).getAuthor());
        date.setText(splitDate(mArticles.get(position).getDate()));

        return listItem;
    }

    private String splitDate(String unformattedDate) {

        String[] date = unformattedDate.split("T");

        return date[0];
    }
}
