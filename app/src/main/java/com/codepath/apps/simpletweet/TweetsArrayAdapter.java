package com.codepath.apps.simpletweet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.apps.simpletweet.models.Tweet;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vee on 3/2/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private Context context;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Tweet
        final Tweet tweet = getItem(position);

        // Find or inflate the template
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // Find subview
        RoundedImageView ivProfileImage = (RoundedImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvTweetName);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvTweetUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);

        // Populate data
        tvUsername.setText("@" + tweet.getUser().getScreenName());
        tvName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvTime.setText(tweet.getCreatedAt());
        ivProfileImage.setImageResource(android.R.color.transparent); // clear out old image
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).fit().into(ivProfileImage);

        // Set Listener
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProfileActivity(tweet.getUser().getScreenName());
            }
        });
        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProfileActivity(tweet.getUser().getScreenName());
            }
        });
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProfileActivity(tweet.getUser().getScreenName());
            }
        });

        // Return
        return convertView;
    }

    private void initProfileActivity(String screenName){
        Intent i = new Intent(context, ProfileActivity.class);
        i.putExtra("screen_name", screenName);
        context.startActivity(i);
    }

}
