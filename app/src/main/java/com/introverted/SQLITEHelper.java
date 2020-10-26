package com.introverted;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLITEHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="AppData.db";
    private static final String LL_TABLE ="longitudeandLatitude";

    public SQLITEHelper(@Nullable Context context) {
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LL_TABLE + "(ID INTEGER PRIMARY KEY, LONGITUDE REAL, LATITUDE REAL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LL_TABLE);
        onCreate(db);
    }

    public boolean insertData(int id, Double latitude, Double longitude){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("LONGITUDE", longitude);
        contentValues.put("LATITUDE", latitude);
        long result = sqLiteDatabase.insert(LL_TABLE, null, contentValues);
        return (result == -1)?(false):(true);
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LL_TABLE + " WHERE ID = " + id, null);
        return cursor;
    }
}
