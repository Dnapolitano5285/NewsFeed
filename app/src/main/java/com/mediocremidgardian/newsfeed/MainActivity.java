package com.mediocremidgardian.newsfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener {

    public static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search";

    ArrayList<Article> mArticles;
    ListView articleListView;
    ArticleAdapter mAdapter;
    TextView emptyView;
    ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleListView = (ListView)findViewById(R.id.list);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        emptyView = (TextView)findViewById(R.id.empty_content);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);

        mRefreshLayout.setOnRefreshListener(this);
        getSupportLoaderManager().initLoader(1,null,this);
    }

    public void moreInfo(int listItem){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mArticles.get(listItem).getWebUrl()));
        startActivity(browserIntent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder= baseUri.buildUpon();

        uriBuilder.appendQueryParameter("order-by","relevance");
        uriBuilder.appendQueryParameter("show-fields","byline");
        uriBuilder.appendQueryParameter("page", "1");
        uriBuilder.appendQueryParameter("q","climate change");
        uriBuilder.appendQueryParameter("api-key","test");

        Log.v("SEARCH URL", uriBuilder.toString());

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        mArticles = (ArrayList) data;
        mProgressBar.setVisibility(View.GONE);

        if (mArticles!=null) {
            mAdapter = new ArticleAdapter(this,R.layout.list_item,mArticles);

            articleListView.setAdapter(mAdapter);
            emptyView.setText(R.string.blank_screen);
            articleListView.setEmptyView(emptyView);

            articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    moreInfo(position);

                }
            });

            mRefreshLayout.setRefreshing(false);

        } else {
            Toast.makeText(getApplicationContext(), R.string.connection_error,Toast.LENGTH_LONG).show();
            mRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        loader.reset();
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(1,null,this);
    }
}
