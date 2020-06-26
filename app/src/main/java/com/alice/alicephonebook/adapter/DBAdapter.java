package com.alice.alicephonebook.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.alice.alicephonebook.bean.Contact;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    private static final String DB_NAME = "contact.db";
    private static final String DB_TABLE = "contactinfo";
    private static final int DB_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";

    private SQLiteDatabase db;
    private final Context context;
    private DBOpenHelper dbOpenHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        private static final String DB_CREATE = "create table " +
                DB_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " +
                KEY_NAME+ " text not null, " + KEY_PHONE + " text," + KEY_ADDRESS + " text);";

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(_db);
        }

    }

    public DBAdapter(Context _context) {
        context = _context;
    }

    public void open() throws SQLiteException {
        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        }catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    public void close() {
        if (db != null){
            db.close();
            db = null;
        }
    }

    public long insert(Contact contact) {
        ContentValues newValues = new ContentValues();

        newValues.put(KEY_NAME, contact.getName());
        newValues.put(KEY_PHONE, contact.getPhone());
        newValues.put(KEY_ADDRESS, contact.getAddress());

        return db.insert(DB_TABLE, null, newValues);

    }
    public long deleteAllData() {
        return db.delete(DB_TABLE, null, null);
    }
    public long deleteOneData(int id) {
        return db.delete(DB_TABLE,  KEY_ID + "=" + id, null);
    }
    public List<Contact> queryAllData() {
        System.out.println(db);
        Cursor results = db.query(DB_TABLE, new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_ADDRESS}, null, null, null, null, null);
        return ConvertToContact(results);
    }
    public List<Contact> queryOneData(int id) {
        Cursor results =  db.query(DB_TABLE, new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_ADDRESS}, KEY_ID + "=" + id, null, null, null, null);
        return ConvertToContact(results);

    }
    public long updateOneData(int id , Contact contact){
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_NAME, contact.getName());
        updateValues.put(KEY_PHONE, contact.getPhone());
        updateValues.put(KEY_ADDRESS, contact.getAddress());

        return db.update(DB_TABLE, updateValues,  KEY_ID + "=" + id, null);
    }

    private List<Contact> ConvertToContact(Cursor cursor){
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()){
            return null;
        }
        List<Contact> contacts=new ArrayList<>();
        for (int i = 0 ; i<resultCounts; i++){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            contact.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            contacts.add(contact);
            cursor.moveToNext();
        }
        return contacts;
    }
}

