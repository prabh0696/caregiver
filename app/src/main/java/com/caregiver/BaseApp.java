package com.caregiver;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.caregiver.core.Constants;
import com.caregiver.core.models.User;
import com.caregiver.database.AppTableInfo;

public class BaseApp extends Application {

    public void onCreate() {
        super.onCreate();
        checkEmptyDataBase();
    }

    private void checkEmptyDataBase() {

        try {
            SQLiteDatabase database = Constants.getDataBaseObj(getApplicationContext());
            if (AppTableInfo.isDataBaseEmpty(database))
                dataBaseEmptyAction(database);

        } catch (Exception e) {
            Log.d("req","Exception: " + e + " ::BaseApp::checkEmptyDataBase");
        }
    }

    private void dataBaseEmptyAction(SQLiteDatabase database) {
        try {
            User user = new User();
            user.First_Name = "Admin";
            user.User_Type = Constants.USER_TYPE_ADMIN;
            user.Email = "admin@caregiver.com";
            user.Password = "12345";
            AppTableInfo.createUser(database, user);
        } catch (Exception e) {
            Log.d("req","Exception: " + e + " ::BaseApp::dataBaseEmptyAction");
        }
    }
}


