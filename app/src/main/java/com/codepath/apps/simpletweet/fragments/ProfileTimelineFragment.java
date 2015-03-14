package com.codepath.apps.simpletweet.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by vee on 3/12/15.
 */
public class ProfileTimelineFragment extends TweetsListFragment {
    private final String TAG = getClass().getSimpleName().toString();
    private TwitterClient client;
    private User owner;
    private String screenName;
    protected boolean isLoading = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        screenName = getArguments().getString("screen_name");
    }

    protected void fetchTimeline(){
        client.getOwner(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                owner = User.fromJSON(response);
                fetchUserTimeline(owner.getUid());
            }
        });
    }

    protected void fetchUserTimeline(long userId){
        if(!isLoading) {
            isLoading = true;
            Log.d(TAG, "populate ProfileTimeline");
            client.getUserTimeline(lastId, screenName, new JsonHttpResponseHandler() {
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

    public static ProfileTimelineFragment newInstance(String screenName) {
        ProfileTimelineFragment userTimelineFragment = new ProfileTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }
}
