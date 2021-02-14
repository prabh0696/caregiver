package com.caregiver.core;

import com.caregiver.core.models.User;

public class Constants {

    public static User loginUser = null;

    public static int USER_TYPE_ADMIN = 0,
            USER_TYPE_GENERAL = 1,
            USER_TYPE_NURSE = 2,
            USER_TYPE_NANEY = 3;


    public static String TAG = "req_tag",
            key_user_type = "user_type",
            key_user = "user",
            key_is_from_signup = "is_from_signup",
            key_is_from_edit = "is_from_edit";


    public static String MOVE_TO_HOME_ACTION = "Move_to_home",
            MOVE_TO_LOGIN_ACTION = "Move_to_login",
            PROFILE_UPDATED_ACTION = "profile_updated",
            BOOKING_ADDED_ACTION = "booking_added";

}
