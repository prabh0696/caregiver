package com.caregiver.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.caregiver.core.models.Address;
import com.caregiver.core.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public final class AppTableInfo {

    //Profile table
    private static final String TABLE_NAME_USER = "User",
            TABLE_USER_COLUMN_ID = "ID",
            TABLE_USER_FIRST_NAME = "First_Name",
            TABLE_USER_LAST_NAME = "Last_Name",
            TABLE_USER_PASSWORD = "Password",
            TABLE_USER_TYPE = "User_Type",
            TABLE_USER_EMAIL = "Email",
            TABLE_USER_PHONE = "Phone",
            TABLE_USER_PHOTO = "Photo",
            TABLE_USER_CHARGES = "Charges",
            TABLE_USER_EXPERIENCE = "Experience",
            TABLE_USER_QUALIFICATION = "Qualification",
            TABLE_USER_ABOUT = "about",
            TABLE_USER_ADDRESS = "Address",
            TABLE_USER_LANDMARK = "landmark",
            TABLE_USER_STREET = "Street",
            TABLE_USER_CITY = "City",
            TABLE_USER_STATE = "State",
            TABLE_USER_COUNTRY = "Country",
            TABLE_USER_PINCODE = "Pincode";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(createUserTableQuery());
    }

    private static String createUserTableQuery() {

        // Database creation SQL statement
        String DATABASE_CREATE = "create table if not exists "
                + TABLE_NAME_USER
                + "("
                + TABLE_USER_COLUMN_ID + " integer primary key autoincrement, "
                + TABLE_USER_FIRST_NAME + " text not null, "
                + TABLE_USER_LAST_NAME + " text not null,"
                + TABLE_USER_EMAIL + " text not null,"
                + TABLE_USER_PHONE + " text not null, "
                + TABLE_USER_PHOTO + " text not null, "
                + TABLE_USER_PASSWORD + " text not null, "
                + TABLE_USER_TYPE + " integer not null, "
                + TABLE_USER_EXPERIENCE + " text not null, "
                + TABLE_USER_CHARGES + " text not null, "
                + TABLE_USER_QUALIFICATION + " text not null, "
                + TABLE_USER_ABOUT + " text not null, "
                + TABLE_USER_ADDRESS + " text not null, "
                + TABLE_USER_LANDMARK + " text not null, "
                + TABLE_USER_STREET + " text not null, "
                + TABLE_USER_CITY + " text not null, "
                + TABLE_USER_STATE + " text not null, "
                + TABLE_USER_COUNTRY + " text not null, "
                + TABLE_USER_PINCODE + " text not null"
                + ");";

        return DATABASE_CREATE;
    }


    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        /*Log.w(TodoTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion                + ", which will destroy all old data");*/

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);

    }


    public static long createUser(SQLiteDatabase db, User pi) {

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TABLE_USER_FIRST_NAME, pi.First_Name);
            values.put(TABLE_USER_LAST_NAME, pi.Last_Name);
            values.put(TABLE_USER_EMAIL, pi.Email);
            values.put(TABLE_USER_PHONE, pi.Phone);
            values.put(TABLE_USER_TYPE, pi.User_Type);
            values.put(TABLE_USER_PASSWORD, pi.Password);
            values.put(TABLE_USER_PHOTO, pi.Photo);


            values.put(TABLE_USER_CHARGES, pi.Charges);
            values.put(TABLE_USER_EXPERIENCE, pi.Experience);
            values.put(TABLE_USER_QUALIFICATION, pi.Qualification);
            values.put(TABLE_USER_ABOUT, pi.about);

            pi.addr = new Address();
            if (pi.addr != null) {
                values.put(TABLE_USER_ADDRESS, pi.addr.Address);
                values.put(TABLE_USER_LANDMARK, pi.addr.landmark);
                values.put(TABLE_USER_STREET, pi.addr.Street);
                values.put(TABLE_USER_CITY, pi.addr.City);
                values.put(TABLE_USER_STATE, pi.addr.State);
                values.put(TABLE_USER_COUNTRY, pi.addr.Country);
                values.put(TABLE_USER_PINCODE, pi.addr.Pincode);
            }

            // Insert the new row, returning the primary key value of the new row
            return db.insert(TABLE_NAME_USER, null, values);
        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::AppTableInfo::createRoute");
        }
        return -1;
    }

    public static void updateUserDetail(SQLiteDatabase db, User pi) {
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_USER_FIRST_NAME, pi.First_Name);
            values.put(TABLE_USER_LAST_NAME, pi.Last_Name);
            values.put(TABLE_USER_EMAIL, pi.Email);
            values.put(TABLE_USER_PHONE, pi.Phone);

            values.put(TABLE_USER_PHOTO, pi.Photo);
            values.put(TABLE_USER_CHARGES, pi.Charges);
            values.put(TABLE_USER_EXPERIENCE, pi.Experience);
            values.put(TABLE_USER_QUALIFICATION, pi.Qualification);
            values.put(TABLE_USER_ABOUT, pi.about);

            if (pi.addr != null) {
                values.put(TABLE_USER_ADDRESS, pi.addr.Address);
                values.put(TABLE_USER_LANDMARK, pi.addr.landmark);
                values.put(TABLE_USER_STREET, pi.addr.Street);
                values.put(TABLE_USER_CITY, pi.addr.City);
                values.put(TABLE_USER_STATE, pi.addr.State);
                values.put(TABLE_USER_COUNTRY, pi.addr.Country);
                values.put(TABLE_USER_PINCODE, pi.addr.Pincode);
            }

            // Which row to update, based on the title
            String selection = TABLE_USER_COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(pi.id)};

            db.update(
                    TABLE_NAME_USER,
                    values,
                    selection,
                    selectionArgs);
        } catch (Exception e) {
            Log.d("req", "Exception: " + e + " ::AppTableInfo::updateUserDetail");
        }
    }

    public static User doLogin(SQLiteDatabase db, String email, String password) {

        // status = 1 for Started
        // status = 2 for Finished

        Cursor cursor = null;
        User user = null;
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    TABLE_USER_COLUMN_ID,
                    TABLE_USER_FIRST_NAME,
                    TABLE_USER_LAST_NAME,
                    TABLE_USER_EMAIL,
                    TABLE_USER_PHONE,
                    TABLE_USER_PHOTO,
                    TABLE_USER_TYPE,
                    TABLE_USER_CHARGES,
                    TABLE_USER_EXPERIENCE,
                    TABLE_USER_QUALIFICATION,
                    TABLE_USER_ABOUT,
                    TABLE_USER_ADDRESS,
                    TABLE_USER_LANDMARK,
                    TABLE_USER_STREET,
                    TABLE_USER_CITY,
                    TABLE_USER_STATE,
                    TABLE_USER_COUNTRY,
                    TABLE_USER_PINCODE
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = null;
            String[] selectionArgs = null;
            selection = TABLE_USER_EMAIL + " = ?"
                    + " AND " + TABLE_USER_PASSWORD + " = ?";
            selectionArgs = new String[2];
            selectionArgs[0] = email;
            selectionArgs[1] = password;

            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            //TABLE_FOLDERS_COLUMN_NAME + " DESC";

            cursor = db.query(
                    TABLE_NAME_USER,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do {
                    try {
                        user = new User();
                        user.id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_USER_COLUMN_ID));
                        user.First_Name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_FIRST_NAME));
                        user.Last_Name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_LAST_NAME));
                        user.Email = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_EMAIL));
                        user.Phone = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_PHONE));
                        user.Photo = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_PHOTO));
                        user.User_Type = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_USER_TYPE));
                        user.Charges = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_CHARGES));

                        user.Experience = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_EXPERIENCE));
                        user.Qualification = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_QUALIFICATION));
                        user.about = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_ABOUT));

                        Address addr = new Address();
                        addr.Address = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_ADDRESS));
                        addr.landmark = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_LANDMARK));
                        addr.Street = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_STREET));
                        addr.City = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_CITY));
                        addr.State = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_STATE));
                        addr.Country = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_COUNTRY));
                        addr.Pincode = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_PINCODE));

                        user.addr = addr;

                    } catch (Exception e) {
                        Log.d("req","Exception: " + e + " ::AppTableInfo::checkUserExist_1");
                    }
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d("req","Exception: " + e + " ::AppTableInfo::checkUserExist_2");
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                Log.d("req","Exception: " + e + " ::AppTableInfo::checkUserExist_3");
            }
        }
        return user;
    }

    public static User checkUserExist(SQLiteDatabase db, String email, String phone) {

        Cursor cursor = null;
        User user = null;
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    TABLE_USER_COLUMN_ID,
                    TABLE_USER_EMAIL,
                    TABLE_USER_PHONE,
                    TABLE_USER_TYPE,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = null;
            String[] selectionArgs = null;
            selection = TABLE_USER_EMAIL + " = ?"
                    + " OR " + TABLE_USER_PHONE + " = ?";
            selectionArgs = new String[2];
            selectionArgs[0] = email;
            selectionArgs[1] = phone;

            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            //TABLE_FOLDERS_COLUMN_NAME + " DESC";

            cursor = db.query(
                    TABLE_NAME_USER,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                do {
                    try {
                        user = new User();
                        user.id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_USER_COLUMN_ID));
                        user.Email = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_EMAIL));
                        user.Phone = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_USER_PHONE));
                    } catch (Exception e) {

                    }
                }
                while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d("req","Exception: " + e + " ::AppTableInfo::checkUserExist");
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                Log.d("req","Exception: " + e + " ::AppTableInfo::checkUserExist_2");
            }
        }
        return user;
    }

    public static boolean isDataBaseEmpty(SQLiteDatabase db) {

        Cursor cursor = null;
        boolean result = true;
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    TABLE_USER_COLUMN_ID,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = null;
            String[] selectionArgs = null;
            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            //TABLE_FOLDERS_COLUMN_NAME + " DESC";

            cursor = db.query(
                    TABLE_NAME_USER,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                result = false;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + " ::AppTableInfo::getUserList");
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e + " ::AppTableInfo::getUserList");
            }
        }
        return result;
    }
}
