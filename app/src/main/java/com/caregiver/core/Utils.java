package com.caregiver.core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Patterns;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.caregiver.core.models.User;
import com.caregiver.ui.profile.ProfileActivity;

public class Utils {

    public static boolean validateEmailAddr(String emailAddr){
        //Check for Email
        return Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches();
    }
    public static void logout(Context context){
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(new Intent(Constants.MOVE_TO_LOGIN_ACTION));
    }

    public static int dp2px(double dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void openProfile(Context context, User pi){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(Constants.key_user, pi);
        context.startActivity(intent);
    }
}
