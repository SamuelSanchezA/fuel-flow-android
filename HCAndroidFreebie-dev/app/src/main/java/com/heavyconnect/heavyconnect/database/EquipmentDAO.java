package com.heavyconnect.heavyconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.heavyconnect.heavyconnect.FuelFlowActivity;
import com.heavyconnect.heavyconnect.entities.Equipment;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

/**
 * This class is the bridge between SQLite database and equipment class requests.
 *
 * @author Felipe Porge Xavier - <a href="http://www.felipeporge.com" target="_blank">http://www.felipeporge.com</a>
 */
public class EquipmentDAO {

    // Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.EQUIPS_COLUMN_LCID, SQLiteHelper.EQUIPS_COLUMN_ID,
            SQLiteHelper.EQUIPS_COLUMN_USER, SQLiteHelper.EQUIPS_COLUMN_NAME, SQLiteHelper.EQUIPS_COLUMN_MODEL_NUMBER,
            SQLiteHelper.EQUIPS_COLUMN_ASSET_NUMBER, SQLiteHelper.EQUIPS_COLUMN_STATUS, SQLiteHelper.EQUIPS_COLUMN_HOURS,
            SQLiteHelper.EQUIPS_COLUMN_LONGITUDE, SQLiteHelper.EQUIPS_COLUMN_LATITUDE, SQLiteHelper.EQUIPS_COLUMN_CHANGED,
            SQLiteHelper.EQUIPS_COLUMN_LAST_MODIFICATION,  SQLiteHelper.EQUIPS_COLUMN_BLUETOOTH_ADDRESS};

    private String[] fuel_flow_columns = { SQLiteHelper.EQUIPS_COLUMN_LCID, SQLiteHelper.EQUIPS_COLUMN_ID,
            SQLiteHelper.EQUIPS_DATETIME, SQLiteHelper.EQUIPS_FUEL_FLOW_RATE, SQLiteHelper.EQUIPS_FUEL_FLOW_TOTAL_CONSUMPTION};

    /**
     * Constructor method.
     * @param context - The context.
     */
    public EquipmentDAO(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Opens database connection.
     * @throws SQLException - SQL Exception can occurs when trying to open database.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes database connection.ursor.moveToFirst();
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Stores an equipment in database.
     * @param equip - Equipment to store.
     * @return - Stored equipment.
     */
    public Equipment put(Equipment equip) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.EQUIPS_COLUMN_ID, equip.getId());
        values.put(SQLiteHelper.EQUIPS_COLUMN_USER, equip.getUser());
        values.put(SQLiteHelper.EQUIPS_COLUMN_NAME, equip.getName());
        values.put(SQLiteHelper.EQUIPS_COLUMN_MODEL_NUMBER, equip.getModelNumber());
        values.put(SQLiteHelper.EQUIPS_COLUMN_ASSET_NUMBER, equip.getAssetNumber());
        values.put(SQLiteHelper.EQUIPS_COLUMN_STATUS, equip.getStatus());
        values.put(SQLiteHelper.EQUIPS_COLUMN_HOURS, equip.getEngineHours());
        values.put(SQLiteHelper.EQUIPS_COLUMN_LONGITUDE, equip.getLongitude());
        values.put(SQLiteHelper.EQUIPS_COLUMN_LATITUDE, equip.getLatitude());
        values.put(SQLiteHelper.EQUIPS_COLUMN_CHANGED, equip.getWasChanged()? 1 : 0);
        values.put(SQLiteHelper.EQUIPS_COLUMN_LAST_MODIFICATION, equip.getLast_modified());
        values.put(SQLiteHelper.EQUIPS_COLUMN_BLUETOOTH_ADDRESS, equip.getBluetoothAddress());

        Cursor cursor = null;
        try {
            long insertId = database.insert(SQLiteHelper.TABLE_EQUIPS, null, values);
            cursor = database.query(SQLiteHelper.TABLE_EQUIPS,
                   allColumns, SQLiteHelper.EQUIPS_COLUMN_LCID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            return cursorToEquipment(cursor);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cursor != null)
                cursor.close();
        }
    }

    /**
     * stores the fuel-flow information of an equipment into the fuel flow table in the database
     * @param values
     * @return ContentValues
     */

    //when it comes to the ContentValues object, we are assuming that it contains the equipment
    //  information, fuel_flow_rate and total_fuel_consumption. Within this code, only the
    //  date and time will be inserted into it
    public void put_fuel_flow (ContentValues values)
    {
        /**
         * get the current date information from the phone itself in the yyyy-mm-dd HH:mm:ss format
         * example: 2012-03-13 12:32:12
         */

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        values.put(SQLiteHelper.EQUIPS_DATETIME, formattedDate);

        /*
        Now we will be putting in the information from the values object into the database
         */
        Cursor cursor = null;
        try
        {
            long insertID = database.insert(SQLiteHelper.TABLE_FUEL_FLOW, null, values);
            cursor = database.query(SQLiteHelper.TABLE_FUEL_FLOW, fuel_flow_columns,
                    SQLiteHelper.EQUIPS_COLUMN_LCID + " = " + insertID, null, null, null, null);
            cursor.moveToFirst();
            return; //cursorToContentValues(cursor);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        finally {
            if(cursor != null)
                cursor.close();
        }
    }

    /**
     * This function will be used for retreiving the average fuel rate for an equipment on
     * based on the ID that is passed in
     */

    public Double getAvgFuelFlowRate(int ID)
    {
        Cursor cursor = database.rawQuery("select round(avg(" + SQLiteHelper.EQUIPS_FUEL_FLOW_RATE +
                "), 2) from " + SQLiteHelper.TABLE_FUEL_FLOW + " where " +
                SQLiteHelper.EQUIPS_COLUMN_ID + " = " + ID + ";", null);

        cursor.moveToFirst();
        if(cursor.getCount() == 0 || cursor.getColumnCount() == 0)  //this means that the cursor is pointing to nothing, or its empty
            return 0.0;
        else
            return cursor.getDouble(0);
    }

    /**
     * This function will return the last total fuel consumption information that was put into it
     * based on the ID that was passed in
     */

    public Double getTotalFuelConsumption(int ID)
    {
        Cursor cursor = database.rawQuery("select max(" + SQLiteHelper.EQUIPS_FUEL_FLOW_TOTAL_CONSUMPTION +
                ") from " + SQLiteHelper.TABLE_FUEL_FLOW + " where " + SQLiteHelper.EQUIPS_COLUMN_ID +
                " = " + ID + ";", null);
        cursor.moveToFirst();
        if(cursor.getColumnCount() == 0 || cursor.getCount() == 0)
            return 0.0;
        return cursor.getDouble(0);
    }

    /**
     *  This function will return the row of information for when the it had the highest
     *  fuel flow rate from the table that corresponds to the appropriate ID passed in
     */

    public ContentValues getMaxFuelFlowRate(int ID)
    {
        Cursor cursorInfo = database.rawQuery("select * from " + SQLiteHelper.TABLE_FUEL_FLOW +
                " where " + SQLiteHelper.EQUIPS_COLUMN_ID + " = " + ID +
                " order by " + SQLiteHelper.EQUIPS_FUEL_FLOW_RATE + " DESC limit 1; ", null);
        cursorInfo.moveToFirst();
        return cursorToContentValues(cursorInfo);
    }

    /**
     * This function will return the row information for when the data showed the lowest fuel flow
     * rate from the table that corresponds to the appropriate ID being passed in
     */

    public ContentValues getMinFuelFlowRate(int ID)
    {
        Cursor cursor = database.rawQuery("select * from " + SQLiteHelper.TABLE_FUEL_FLOW
                    + " where " + SQLiteHelper.EQUIPS_COLUMN_ID + " = " + ID + " order by " +
                    SQLiteHelper.EQUIPS_FUEL_FLOW_RATE + " limit 1;", null);

        cursor.moveToFirst();
        if(cursor == null)
            return null;
        else
            return cursorToContentValues(cursor);
    }

    /**
     * Finds an equipment in database.
     * @param id - Equipment name.
     * @return - Stored equipment.
     */

    public Equipment find(int id){
        Cursor cursor = null;
        try {
            cursor = database.query(SQLiteHelper.TABLE_EQUIPS,
                    allColumns, SQLiteHelper.EQUIPS_COLUMN_ID + " = " + id, null, null, null, null);
            cursor.moveToFirst();
            return cursorToEquipment(cursor);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            if(cursor != null)
                cursor.close();
        }
    }

    /**
     * Removes an equipment stored in database.
     * @param id - Equipment id.
     */
    public void remove(int id){
        database.delete(SQLiteHelper.TABLE_EQUIPS,
                SQLiteHelper.EQUIPS_COLUMN_ID + "=" + id, null);
    }

    /**
     * Gets all equipments stored in database.
     * @return - List of equipments.
     */
    public ArrayList<Equipment> getAll() {
        ArrayList<Equipment> result = new ArrayList<Equipment>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_EQUIPS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Equipment equip = cursorToEquipment(cursor);
            result.add(equip);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return result;
    }

    /**
     * Gets all changed equipments stored in database.
     * @return - List of equipments.
     */
    public ArrayList<Equipment> getAllChanged() {
        ArrayList<Equipment> result = new ArrayList<Equipment>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_EQUIPS,
                allColumns, SQLiteHelper.EQUIPS_COLUMN_CHANGED + "=1", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Equipment equip = cursorToEquipment(cursor);
            result.add(equip);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return result;
    }


    /**
     * Updates an equipment stored in database.
     * @param equip - Equipment with new information and valid id.
     * @return - Stored equipment.
     */

    public Equipment update(Equipment equip){
        remove(equip.getId());
        return put(equip);
    }

    /**
     * Gets an equipment using the cursor.
     * @param cursor - Cursor.
     * @return - Equipment.
     */
    private Equipment cursorToEquipment(Cursor cursor) {
        Equipment result = new Equipment();
        result.setId(cursor.getInt(1));
        result.setUser(cursor.getInt(2));
        result.setName(cursor.getString(3));
        result.setModelNumber(cursor.getString(4));
        result.setAssetNumber(cursor.getInt(5));
        result.setStatus(cursor.getInt(6));
        result.setEngineHours(cursor.getInt(7));
        result.setLongitude(cursor.getDouble(8));
        result.setLatitude(cursor.getDouble(9));
        result.setWasChanged(cursor.getInt(10) == 1 ? true : false);
        result.setLast_modified(cursor.getString(11));

        return result;
    }

    /**
     *  This function will convert a cursor pointer information into a content that will then be
     *  returned back to where it was called.
     * @param cursor
     * @return content
     */

    private ContentValues cursorToContentValues(Cursor cursor) {
        ContentValues content = new ContentValues();
        if(cursor.getColumnCount() == 0 || cursor.getCount() == 0)  //this will be used to check whether the
            return null;                                            //cursor passed in contains the appropriate information
                                                                    //that we want
        else {  //if it does have the information that we want, then we can work with it
            content.put(SQLiteHelper.EQUIPS_COLUMN_ID, cursor.getInt(1));
            content.put(SQLiteHelper.EQUIPS_DATETIME, cursor.getString(2));
            content.put(SQLiteHelper.EQUIPS_FUEL_FLOW_RATE, cursor.getDouble(3));
            content.put(SQLiteHelper.EQUIPS_FUEL_FLOW_TOTAL_CONSUMPTION, cursor.getDouble(4));
            return content;
        }
    }

    /**
     * Clear database.
     */

    public void removeAll(){
        database.delete(SQLiteHelper.TABLE_EQUIPS, null, null);
        database.delete(SQLiteHelper.TABLE_FUEL_FLOW, null, null);
    }

    /**
     * Stores all equipments in database.
     * @param equips - Equipments to store.
     */

    public void putAll(ArrayList<Equipment> equips){
        if(equips == null)
            return;

        for(Equipment eq : equips){
            if(eq == null)
                continue;

            put(eq);
        }
    }



}
