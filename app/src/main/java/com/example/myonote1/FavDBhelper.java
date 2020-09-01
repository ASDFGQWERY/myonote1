package com.example.myonote1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavDBhelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "NEKO6_DB";
    private static int DB_VERSION =1;
    public static final String TABLE_NAME = "NEKO6_TABLE";
    public static String PKID = "id";
    public static String UUID = "uuid";
    public static String BODY = "body";
    public static String DBTIME = "dbTime";
    //public static String ITEM_IMAGE = "itemImage";
    public static String FAVORITE_STATUS = "favStatus";


    public FavDBhelper(Context context) { super(context, DATABASE_NAME, null, DB_VERSION);}



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                PKID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UUID + " TEXT, " +
                BODY + " TEXT, " +
                FAVORITE_STATUS + " TEXT, " +
                DBTIME + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }



    // select all favorite list
    public Cursor select_all_favorite_list(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE favStaus = 2";
        return db.rawQuery(sql, null,null);
    }

    // read all data
    public Cursor read_all_data(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + UUID + "="+uuid+"";
        return db.rawQuery(sql, null,null);
    }



}
