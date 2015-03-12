package com.codepath.apps.simpletweet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweet.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweet.fragments.MentionTimelineFragment;
import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.apps.simpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class TweetActivity extends ActionBarActivity {

    private TwitterClient client;
    private User owner;
    private Context context;
    private EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        client = TwitterApplication.getRestClient();
        context = getApplicationContext();
        setOwner();
    }

    private void setOwner() {
        Log.i("DEBUG", "Set Owner");
        client.getOwner(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", response.toString());
                owner = User.fromJSON(response);
                setupViews();
            }
        });
    }

    private void setupViews() {
        etTweet = (EditText) findViewById(R.id.etTweet);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        RoundedImageView ivProfilePicture = (RoundedImageView) findViewById(R.id.ivProfilePicture);

        tvName.setText(owner.getName());
        tvUsername.setText("@" + owner.getScreenName());
        Picasso.with(context).load(owner.getProfileImageUrl()).fit().into(ivProfilePicture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_post_tweet) {
            String tweetBody = etTweet.getText().toString();
            client.postTweet(tweetBody, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Tweet resultTweet = Tweet.fromJSON(response);
                    Intent i = new Intent();
                    i.putExtra("newTweet", resultTweet);
                    setResult(RESULT_OK, i);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(context, "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
