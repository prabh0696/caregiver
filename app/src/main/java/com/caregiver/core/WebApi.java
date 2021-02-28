package com.caregiver.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class WebApi {
    private static final String BASE_URL = "http://www.saamsoft.com/new/caregiver/index.php?";
    public static final String IMAGE_BASE_URL = "http://www.saamsoft.com/new/caregiver/users/";
    public static final String LOGIN = BASE_URL + "action=login",
            FORGOT_PASS = BASE_URL + "action=user_exist",
            SIGNUP = BASE_URL + "action=register",
            UPDATE_PROFILE = BASE_URL + "action=profile",
            GET_USER_LIST = BASE_URL + "action=users",
            ADD_BOOKING = BASE_URL + "action=add_booking",
            GET_BOOKING_LIST = BASE_URL + "action=all_bookings",
            UPDATE_BOOKING = BASE_URL + "action=update_booking",
            DELETE_USER = BASE_URL + "action=delete_user",
            ADD_REVIEW = BASE_URL + "action=add_review",
            USER_DETAIL = BASE_URL + "action=user_detail";


    private static ProgressDialog loadingDialog;
    private static Context mContext;

    public static void showLongToast(Context context, String message) {

        dismissLoadingDialog();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    public static void showLoadingDialog(Context context) {
        mContext = context;
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }
        loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage("Loading. Please wait...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }

    public static void setLoadingMessage(String message) {
        if (loadingDialog != null)
            loadingDialog.setMessage(message);
    }

    public static void dismissLoadingDialog() {

        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }

    }
}
