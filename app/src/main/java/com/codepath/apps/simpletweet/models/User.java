package com.codepath.apps.simpletweet.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by vee on 3/2/15.
 */
public class User implements Serializable {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private int followersCount;
    private String tagline;


    private int followingCount;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public static User fromJSON(JSONObject json) {
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.followersCount = json.getInt("followers_count");
            u.followingCount = json.getInt("friends_count");
            u.tagline = json.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;

    }
}
/*
    "user": {
      "name": "Raffi Krikorian",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": false,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/1270234259/raffi-headshot-casual_normal.png",
      "created_at": "Sun Aug 19 14:24:06 +0000 2007",
      "location": "San Francisco, California",
      "follow_request_sent": false,
      "id_str": "8285392",
      "is_translator": false,
      "profile_link_color": "0084B4",
      "entities": {
        "url": {
          "urls": [
            {
              "expanded_url": "http://about.me/raffi.krikorian",
              "url": "http://t.co/eNmnM6q",
              "indices": [0,19],
              "display_url": "about.me/raffi.krikorian"
            }
          ]
        },
        "description": {"urls": []}
      },
            "default_profile": false,
      "url": "http://t.co/Lxw7upbN",
      "contributors_enabled": false,
      "favourites_count": 15990,
      "utc_offset": -28800,
      "profile_image_url_https": "https://si0.twimg.com/profile_images/2546730059/f6a8zq58mg1hn0ha8vie_normal.jpeg",
      "id": 819797,
      "listed_count": 340,
      "profile_use_background_image": true,
      "profile_text_color": "D20909",
      "followers_count": 7126,
      "lang": "en",
      "protected": false,
      "geo_enabled": true,
      "notifications": false,
      "description": "Reality Technician, Twitter API team, synthesizer enthusiast; a most excellent adventure in timelines. I know it's hard to believe in something you can't see.",
      "profile_background_color": "000000",
      "verified": false,
      "time_zone": "Pacific Time (US & Canada)",
      "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/643655842/hzfv12wini4q60zzrthg.png",
      "statuses_count": 18076,
      "profile_background_image_url": "http://a0.twimg.com/profile_background_images/643655842/hzfv12wini4q60zzrthg.png",
      "default_profile_image": false,
      "friends_count": 5444,
      "following": true,
      "show_all_inline_media": true,
      "screen_name": "episod"
    }
 */