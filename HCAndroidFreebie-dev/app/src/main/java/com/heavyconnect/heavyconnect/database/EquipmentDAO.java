package com.heavyconnect.heavyconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.heavyconnect.heavyconnect.entities.Equipment;

import java.util.ArrayList;

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
     * Closes database connection.
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
        result.setWasChanged(cursor.getInt(10) == 1? true : false);
        result.setLast_modified(cursor.getString(11));

        return result;
    }

    /**
     * Clear database.
     */
    public void removeAll(){
        database.delete(SQLiteHelper.TABLE_EQUIPS, null, null);
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
