package com.caregiver.core;

import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class Utils {

    public static boolean validateEmailAddr(String emailAddr){
        //Check for Email
        return Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches();
    }
    public static void logout(Context context){
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(new Intent(Constants.MOVE_TO_LOGIN_ACTION));
    }

}
