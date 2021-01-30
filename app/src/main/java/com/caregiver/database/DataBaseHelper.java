package com.caregiver.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public final class DataBaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "iv_caregiver.db";

    private static DataBaseHelper _dbHelper;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        AppTableInfo.onCreate(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        AppTableInfo.onUpgrade(db, oldVersion, newVersion);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static  DataBaseHelper getInstance(Context mContext) {
        if (_dbHelper == null) {
            synchronized (DataBaseHelper.class){
                if(_dbHelper == null){
                    _dbHelper = new DataBaseHelper(mContext);
                }
            }
        }
        return _dbHelper;
    }
}
