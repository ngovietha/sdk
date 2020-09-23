/*
 * Created by NQC on 4/25/20 7:25 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/25/20 7:25 PM
 *
 */

package com.nqc.idoctor;

/**
 * AppConfig
 */
public class AppConfig {
    public static final String  BASE_URL="https://vnexpress.net/";
    public static final String API_AUTH_HEADER_KEY_NAME = "Authorization";
    public static final String API_KEY = "Put your api key here";
    public static final String FB_FIELDS = "id, name, first_name, last_name, middle_name, name_format, picture, short_name, email, gender, birthday ,friends, hometown, likes, link, location, photos, posts, videos";
    public static final String[] FB_PERMISSION = new String[]{
            "email",
            "user_birthday",
            "user_events",
            "user_friends",
            "user_gender",
            "user_hometown",
            "user_likes",
            "user_link",
            "user_location",
            "user_photos",
            "user_posts",
            "user_videos"
    };

}
