package com.spotapps.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tty on 1/7/2016.
 */
public class LoadedSpotsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private SpotSQLiteHelper dbHelper;
    private String[] allColumns = { SpotSQLiteHelper.COLUMN_ID,
            SpotSQLiteHelper.COLUMN_SPOT_JSON };

    public LoadedSpotsDataSource(Context context) {
        dbHelper = new SpotSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public LoadedSpot createLoadedSpot(String spotJson) {
        ContentValues values = new ContentValues();
        values.put(SpotSQLiteHelper.COLUMN_SPOT_JSON, spotJson);
        long insertId = database.insert(SpotSQLiteHelper.TABLE_SPOTS, null,
                values);
        Cursor cursor = database.query(SpotSQLiteHelper.TABLE_SPOTS,
                allColumns, SpotSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        LoadedSpot spot = cursorToLoadedSpot(cursor);
        cursor.close();
        return spot;
    }

    public void deleteLoadedSpot(LoadedSpot spot) {
        long id = spot.getId();
        // System.out.println("LoadedSpot deleted with id: " + id);
        database.delete(SpotSQLiteHelper.TABLE_SPOTS, SpotSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<LoadedSpot> getAllLoadedSpots() {
        List<LoadedSpot> spots = new ArrayList<LoadedSpot>();

        Cursor cursor = database.query(SpotSQLiteHelper.TABLE_SPOTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LoadedSpot spot = cursorToLoadedSpot(cursor);
            spots.add(spot);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return spots;
    }

    private LoadedSpot cursorToLoadedSpot(Cursor cursor) {
        LoadedSpot spot = new LoadedSpot();
        spot.setId(cursor.getLong(0));
        spot.setSpot_json(cursor.getString(1));
        return spot;
    }
}
