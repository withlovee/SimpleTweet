package com.codepath.apps.simpletweet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * Created by vee on 3/9/15.
 */
public class HomeTimelineFragment extends TweetsListFragment {
    private final String TAG = getClass().getSimpleName().toString();
    private TwitterClient client;
    protected boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    protected void fetchTimeline(){
        if(!isLoading) {
            isLoading = true;
            Log.d(TAG, "populate HomeTimeline");
            client.getHomeTimeline(lastId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    populateData(response);
                    isLoading = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    turnOffRefreshing();
                    isLoading = false;
                }
            });
        }
    }


}
