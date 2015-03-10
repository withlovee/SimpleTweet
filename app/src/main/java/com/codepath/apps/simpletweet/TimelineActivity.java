package com.codepath.apps.simpletweet;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private Tweet newTweet = null;
    public final int TWEET_PAGE = 100;
    private long lastId = 0;
    private SwipeRefreshLayout swipeContainer;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        client = TwitterApplication.getRestClient();
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        setupPullToRefresh();
        populateTimeline();
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("DEBUG", "onLoadMore " + page);
                if(!isLoading){
                    Log.i("DEBUG", "loading " + lastId);
                    isLoading = true;
                    loadMoreTimeLine();
                }
            }
        });
    }

    private void setupPullToRefresh(){
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.primary_light);
    }

    private void loadMoreTimeLine(){
        client.getHomeTimeline(lastId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Deserialize JSON
                // Create Models
                // Load the model into ListView
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                lastId = tweets.get(tweets.size()-1).getUid();
                aTweets.addAll(tweets);
                //Log.i("DEBUG", response.toString());
                Log.i("DEBUG", lastId + " " + tweets.get(tweets.size()-1).getBody());
                aTweets.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                isLoading = false;
            }
        });
    }

    private void populateTimeline(){
        client.getHomeTimeline(0, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Deserialize JSON
                // Create Models
                // Load the model into ListView
                aTweets.clear();
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                lastId = tweets.get(tweets.size()-1).getUid();
                aTweets.addAll(tweets);
                //Log.i("DEBUG", response.toString());
                Log.i("DEBUG", lastId + " " + tweets.get(tweets.size()-1).getBody());
                aTweets.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("DEBUG", "onActivityResult");
        if(resultCode == RESULT_OK && requestCode == TWEET_PAGE) {
            aTweets.clear();
            Tweet newTweet = (Tweet) data.getSerializableExtra("newTweet");
            populateTimeline();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tweet) {
            Intent i = new Intent(TimelineActivity.this, TweetActivity.class);
            startActivityForResult(i, TWEET_PAGE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
