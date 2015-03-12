package com.codepath.apps.simpletweet.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.simpletweet.EndlessScrollListener;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TweetsArrayAdapter;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vee on 3/9/15.
 */
public abstract class TweetsListFragment extends Fragment {
    private final String TAG = getClass().getSimpleName().toString();
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    protected long lastId = 0;
    private SwipeRefreshLayout swipeContainer;

    /*public static TweetsListFragment newInstance(String screenName) {
        TweetsListFragment tweetsListFragment = new TweetsListFragment();
        Bundle args = new Bundle();
    }*/

    // is called first
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("DEBUG", "onLoadMore " + page);
                fetchTimeline();
            }
        });

        setupPullToRefresh();
        fetchTimeline();

        return v;
    }

    public void refresh(){
        aTweets.clear();
        lastId = 0;
        Log.d(TAG, "Populate timeline again");
        fetchTimeline();
    }

    private void setupPullToRefresh(){
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.primary_light);
    }

    protected abstract void fetchTimeline();

    protected void populateData(JSONArray response){
        // Deserialize JSON
        // Create Models
        // Load the model into ListView
        ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
        lastId = tweets.get(tweets.size() - 1).getUid();
        aTweets.addAll(tweets);
        Log.i("DEBUG", lastId + " " + tweets.get(tweets.size() - 1).getBody());
        aTweets.notifyDataSetChanged();
        turnOffRefreshing();
    }

    public void turnOffRefreshing(){
        swipeContainer.setRefreshing(false);
    }
}
