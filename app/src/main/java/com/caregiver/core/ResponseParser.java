package com.caregiver.core;

import android.util.Log;

import com.caregiver.core.models.Address;
import com.caregiver.core.models.Booking;
import com.caregiver.core.models.Review;
import com.caregiver.core.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class ResponseParser {

    public static User parseLoginResponse(String response) {
        Log.d("file_tag", "parseApiStatus::response = " + response);
        User user = null;
        try {
            JSONObject jObj = new JSONObject(response);
            if(jObj.has("error_code")){
                return null;
            }

            JSONObject obj = new JSONObject(jObj.getString("data"));
            user = new User();
            user.id = obj.getInt("id");
            user.First_Name = obj.getString("first_name");
            user.Last_Name = obj.getString("last_name");
            user.Password = obj.getString("password");
            user.User_Type = obj.getInt("user_type");
            user.Email = obj.getString("email");
            user.Phone = obj.getString("phone");
            user.Photo = obj.getString("photo");
            user.Charges = obj.getString("charges");
            user.Experience = obj.getString("experience");
            user.Qualification = obj.getString("qualification");
            user.about = obj.getString("about");

            Address address = new Address();
            address.Address = obj.getString("address");
            address.landmark = obj.getString("landmark");
            address.Street = obj.getString("street");
            address.City = obj.getString("city");
            address.State = obj.getString("state");
            address.Country = obj.getString("country");
            address.Pincode = obj.getString("pincode");

            user.addr = address;
        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::ResponseParser::parseFileUpload");
        }
        return user;
    }

    public static User parseSignupResponse(String response) {
        Log.d("file_tag", "parseApiStatus::response = " + response);
        User user = null;
        try {
            JSONObject jObj = new JSONObject(response);
            if(jObj.has("error_code") && jObj.getString("error_code").equalsIgnoreCase("101")){
                return user;
            }
            JSONObject obj = new JSONObject(jObj.getString("data"));
            user = new User();
            user.id = obj.getInt("id");
            user.First_Name = obj.getString("first_name");
            user.Last_Name = obj.getString("last_name");
            user.Password = obj.getString("password");
            user.User_Type = obj.getInt("user_type");
            user.Email = obj.getString("email");
            user.Phone = obj.getString("phone");
            user.Photo = obj.getString("photo");
            user.Charges = obj.getString("charges");
            user.Experience = obj.getString("experience");
            user.Qualification = obj.getString("qualification");
            user.about = obj.getString("about");

            Address address = new Address();
            address.Address = obj.getString("address");
            address.landmark = obj.getString("landmark");
            address.Street = obj.getString("street");
            address.City = obj.getString("city");
            address.State = obj.getString("state");
            address.Country = obj.getString("country");
            address.Pincode = obj.getString("pincode");

            user.addr = address;


        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::ResponseParser::parseFileUpload");
        }
        return user;
    }

    public static List<User> parseUserList(String response) {
        Log.d("file_tag", "parseApiStatus::response = " + response);
        List<User> list = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(response);
            JSONArray jArr = jObj.getJSONArray("data");
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                User user = new User();
                user.id = obj.getInt("id");
                user.First_Name = obj.getString("first_name");
                user.Last_Name = obj.getString("last_name");
                user.User_Type = obj.getInt("user_type");
                user.Email = obj.getString("email");
                user.Phone = obj.getString("phone");
                user.Photo = obj.getString("photo");
                user.Charges = obj.getString("charges");
                user.Experience = obj.getString("experience");
                user.Qualification = obj.getString("qualification");
                user.about = obj.getString("about");
                user.availability = obj.getInt("availability");

                Address address = new Address();
                address.Address = obj.getString("address");
                address.landmark = obj.getString("landmark");
                address.Street = obj.getString("street");
                address.City = obj.getString("city");
                address.State = obj.getString("state");
                address.Country = obj.getString("country");
                address.Pincode = obj.getString("pincode");

                user.addr = address;
                list.add(user);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::ResponseParser::parseUserList");
        }
        return list;
    }

    public static boolean checkUserExist(String response) {
        Log.d("file_tag", "parseApiStatus::response = " + response);
        try {
            JSONObject jObj = new JSONObject(response);
            if(jObj.has("error_code") && jObj.getString("error_code").equalsIgnoreCase("104")){
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::ResponseParser::parseFileUpload");
        }
        return true;
    }

    public static List<Booking> parseBookingList(String response) {
        Log.d("file_tag", "parseApiStatus::response = " + response);
        List<Booking> list = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(response);
            JSONArray jArr = jObj.getJSONArray("data");
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                Booking pi = new Booking();
                pi.booking_id = obj.getString("booking_id");
                pi.review_id = obj.getString("review_id");
                pi.booked_date = obj.getString("booked_date");
                pi.booking_message = obj.getString("booking_message");
                pi.booking_status = obj.getString("booking_status");

                pi.from_date = obj.getString("from_date");
                pi.from_time = obj.getString("from_time");
                pi.from_email = obj.getString("from_email");
                pi.from_first_name = obj.getString("from_first_name");
                pi.from_last_name = obj.getString("from_last_name");
                pi.from_phone = obj.getString("from_phone");
                pi.from_user_id = obj.getString("from_user_id");
                pi.from_photo = obj.getString("from_photo");

                pi.to_date = obj.getString("to_date");
                pi.to_time = obj.getString("to_time");
                pi.to_email = obj.getString("to_email");
                pi.to_first_name = obj.getString("to_first_name");
                pi.to_last_name = obj.getString("to_last_name");
                pi.to_phone = obj.getString("to_phone");
                pi.to_user_id = obj.getString("to_user_id");
                pi.to_photo = obj.getString("to_photo");

                list.add(pi);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::ResponseParser::parseBookingList");
        }
        return list;
    }

    public static List<Review> parseReviewList(String response) {
        Log.d("file_tag", "parseApiStatus::response = " + response);
        List<Review> list = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(response);
            JSONArray jArr = jObj.getJSONArray("data");
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                Review pi = new Review();
                pi.booking_id = obj.getString("booking_id");
                pi.review_date = obj.getString("review_date");
                pi.review = obj.getString("review");
                pi.from_email = obj.getString("from_email");
                pi.from_first_name = obj.getString("from_first_name");
                pi.from_last_name = obj.getString("from_last_name");
                pi.from_phone = obj.getString("from_phone");
                pi.from_user_id = obj.getString("from_user_id");
                pi.from_photo = obj.getString("from_photo");
                pi.to_email = obj.getString("to_email");
                pi.to_first_name = obj.getString("to_first_name");
                pi.to_last_name = obj.getString("to_last_name");
                pi.to_phone = obj.getString("to_phone");
                pi.to_user_id = obj.getString("to_user_id");
                pi.to_photo = obj.getString("to_photo");
                list.add(pi);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::ResponseParser::parseBookingList");
        }
        return list;
    }
}