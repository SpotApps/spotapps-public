package com.spotapps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tty on 1/3/2016.
 */
public class SpotSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SPOTS = "loaded_spots";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SPOT_JSON = "spot_json";

    private static final String DATABASE_NAME = "spot.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SPOTS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SPOT_JSON + " text not null, "
            + ");";

    public SpotSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SpotSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPOTS);
        onCreate(db);
    }
}
