package com.codepath.apps.simpletweet.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by vee on 3/11/15.
 */
public class MentionTimelineFragment extends TweetsListFragment {
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
            Log.d(TAG, "populate MentionTimeline");
            client.getMentionTimeline(lastId, new JsonHttpResponseHandler() {
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
