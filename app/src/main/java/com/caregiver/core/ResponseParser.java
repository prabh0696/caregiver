package com.caregiver.core;

import android.util.Log;

import com.caregiver.core.models.Address;
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


}