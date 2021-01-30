package com.caregiver.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.caregiver.database.DataBaseHelper;

public class Constants {

    public static int loginUserId = 0;

    public static int USER_TYPE_ADMIN = 0,
            USER_TYPE_GENERAL = 1,
            USER_TYPE_NURSE = 2,
            USER_TYPE_NANEY = 3;


    public static String key_user_type = "user_type",
            key_user = "user",
            key_is_from_signup = "is_from_signup",
            key_is_from_edit = "is_from_edit";

    private static SQLiteDatabase _database;

    public static String MOVE_TO_HOME_ACTION = "Move_to_home",
            MOVE_TO_LOGIN_ACTION = "Move_to_login",
            PROFILE_UPDATED_ACTION = "profile_updated";


    public static SQLiteDatabase getDataBaseObj(Context context) {
        if (_database == null || !_database.isOpen()) {
            _database = DataBaseHelper.getInstance(context).getWritableDatabase();
        }
        return _database;
    }
}
