package com.codepath.apps.simpletweet;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweet.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweet.fragments.MentionTimelineFragment;

public class TimelineActivity extends ActionBarActivity {
    private final String TAG = getClass().getSimpleName().toString();
    public final int TWEET_PAGE = 100;
    private ViewPager vpPager;
    protected HomeTimelineFragment homeTimelineFragment;
    protected MentionTimelineFragment mentionTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        homeTimelineFragment = new HomeTimelineFragment();
        mentionTimelineFragment = new MentionTimelineFragment();

        // Get the viewpager
        vpPager = (ViewPager) findViewById(R.id.timeline_viewpager);

        // Set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        // Find the sliding tab
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.timeline_tabs);

        // Attach tabStrip to the viewpager
        tabStrip.setViewPager(vpPager);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("DEBUG", "onActivityResult");
        if(resultCode == RESULT_OK && requestCode == TWEET_PAGE) {
            vpPager.setCurrentItem(0);
            homeTimelineFragment.refresh();
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

        if (id == R.id.action_tweet) {
            Intent i = new Intent(TimelineActivity.this, TweetActivity.class);
            startActivityForResult(i, TWEET_PAGE);
            return true;
        }
        else if (id == R.id.action_profile) {
            Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return homeTimelineFragment;
            } else if(position == 1) {
                return mentionTimelineFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
