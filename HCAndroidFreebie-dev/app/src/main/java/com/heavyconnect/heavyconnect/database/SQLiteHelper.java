package com.heavyconnect.heavyconnect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class represents a helper to control SQLite database.
 *
 * @author Felipe Porge Xavier - <a href="http://www.felipeporge.com" target="_blank">http://www.felipeporge.com</a>
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hc.db";

    public static final String TABLE_EQUIPS = "equips";
    public static final String EQUIPS_COLUMN_LCID = "lcid"; // LOCAL ID - 0
    public static final String EQUIPS_COLUMN_ID = "id"; // 1
    public static final String EQUIPS_COLUMN_USER = "user"; // 2
    public static final String EQUIPS_COLUMN_NAME = "name"; // 3
    public static final String EQUIPS_COLUMN_MODEL_NUMBER = "modelNumber"; //4
    public static final String EQUIPS_COLUMN_ASSET_NUMBER = "assetNumber"; // int - 5
    public static final String EQUIPS_COLUMN_STATUS = "status"; // int - 6
    public static final String EQUIPS_COLUMN_HOURS = "hours"; // int - 7
    public static final String EQUIPS_COLUMN_LONGITUDE = "longitude"; // real - 8
    public static final String EQUIPS_COLUMN_LATITUDE = "latitude"; // real - 9
    public static final String EQUIPS_COLUMN_CHANGED = "changed"; // 10
    public static final String EQUIPS_COLUMN_LAST_MODIFICATION = "last_modification";
    public static final String EQUIPS_COLUMN_BLUETOOTH_ADDRESS = "bluetooth_address";

    public static final String TABLE_FUEL_FLOW = "fuel_flow";
    public static final String EQUIPS_DATETIME = "datetime";    //2
    public static final String EQUIPS_FUEL_FLOW_RATE = "fuel_flow_rate"; //this will store the tractors fuel rate //3
    public static final String EQUIPS_FUEL_FLOW_TOTAL_CONSUMPTION = "total_fuel_consumed"; //this is how much fuel the tractor has consumed overall //4




    private final String SQL_CREATE_EQUIPS_ENTRIES =
            "CREATE TABLE " + TABLE_EQUIPS +
                    "( " +
                    EQUIPS_COLUMN_LCID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  //similar to the RowID def. in databases
                    EQUIPS_COLUMN_ID + " INTEGER, " +
                    EQUIPS_COLUMN_USER + " INTEGER, " +
                    EQUIPS_COLUMN_NAME + " TEXT, " +
                    EQUIPS_COLUMN_MODEL_NUMBER + " TEXT, " +
                    EQUIPS_COLUMN_ASSET_NUMBER + " INTEGER, " +
                    EQUIPS_COLUMN_STATUS + " INTEGER, " +
                    EQUIPS_COLUMN_HOURS + " INTEGER, " +
                    EQUIPS_COLUMN_LONGITUDE + " REAL, " +
                    EQUIPS_COLUMN_LATITUDE + " REAL, " +
                    EQUIPS_COLUMN_CHANGED + " INTEGER, " +
                    EQUIPS_COLUMN_LAST_MODIFICATION + " TEXT, " +
                    EQUIPS_COLUMN_BLUETOOTH_ADDRESS + " TEXT" +
                    "); ";

    private  final String SQL_CREATE_FUEL_FLOW_ENTRIES =
            "CREATE TABLE " + TABLE_FUEL_FLOW +
                    " ( " +
                    EQUIPS_COLUMN_LCID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EQUIPS_COLUMN_ID + " INTEGER, " +
                    EQUIPS_DATETIME + " TEXT, " +
                    EQUIPS_FUEL_FLOW_RATE  + " REAL, " +
                    EQUIPS_FUEL_FLOW_TOTAL_CONSUMPTION + " REAL" +
                    "); ";

    //datetime('now', 'localtime') this is what will be used when inputing the date into the table with using SQLite3

    /**
     * Constructor method.
     * @param context - The context.
     */
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_EQUIPS_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_FUEL_FLOW_ENTRIES);
    }


    /**
     * Delete the table and add another overwriting it
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FUEL_FLOW);
        onCreate(sqLiteDatabase);
    }


}