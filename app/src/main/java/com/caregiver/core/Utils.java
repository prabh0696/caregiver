package com.caregiver.core;

import android.util.Patterns;

public class Utils {

    public static boolean validateEmailAddr(String emailAddr){
        //Check for Email
        return Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches();
    }
}
