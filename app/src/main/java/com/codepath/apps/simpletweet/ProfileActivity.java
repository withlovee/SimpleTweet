package com.codepath.apps.simpletweet;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codepath.apps.simpletweet.fragments.ProfileTimelineFragment;
import com.codepath.apps.simpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    private User owner;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        client = TwitterApplication.getRestClient();

        // Get screen name from previous activity
        String screenName = getIntent().getStringExtra("screen_name");

        setupOwner(screenName);

        if(savedInstanceState == null) {
            // Create UserTimelineFragment
            ProfileTimelineFragment profileTimelineFragment = ProfileTimelineFragment.newInstance(screenName);

            // Display user fragment within this activity dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserContainer, profileTimelineFragment);
            ft.commit();
        }

    }

    private void setupOwner(String screenName) {
        Log.d("Profile", "setupOwner");
        if(screenName == null) {
            client.getOwner(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    createOwnerFromJSON(response);
                }
            });
        }
        else {
            client.getUser(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    createOwnerFromJSON(response);
                }
            });
        }
    }

    private void createOwnerFromJSON(JSONObject response) {
        Log.d("Profile", "createOwnerFromJSON");
        owner = User.fromJSON(response);
        setupView();
    }

    private void setupView() {
        Log.d("Profile", "setupView");
        TextView tvUserUsername = (TextView) findViewById(R.id.tvUserUsername);
        TextView tvUserRealName = (TextView) findViewById(R.id.tvUserRealName);
        TextView tvUserTagline = (TextView) findViewById(R.id.tvUserTagline);
        TextView tvUserFollowersCount = (TextView) findViewById(R.id.tvUserFollowersCount);
        TextView tvUserFollowingCount = (TextView) findViewById(R.id.tvUserFollowingCount);
        RoundedImageView ivUserProfilePicture = (RoundedImageView) findViewById(R.id.ivUserProfilePicture);

        // Populate Data
        tvUserUsername.setText("@" + owner.getScreenName());
        tvUserRealName.setText(owner.getName());
        tvUserTagline.setText(owner.getTagline());
        tvUserFollowersCount.setText(owner.getFollowersCount() + " Followers");
        tvUserFollowingCount.setText(owner.getFollowingCount() + " Following");
        Picasso.with(this).load(owner.getProfileImageUrl()).fit().into(ivUserProfilePicture);

        getSupportActionBar().setTitle("@" + owner.getScreenName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
