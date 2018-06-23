package com.lab3.michau.phones.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.lab3.michau.phones.domain.Phone;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public final static int WERSJA_BAZY = 2;
    public final static String ID = "ID";
    public final static String NAZWA_BAZY = "TELEFONYDB";
    public final static String NAZWA_TABELI = "TELEFONY";
    public final static String KOLUMNA1 = "MODEL";
    public final static String KOLUMNA2 = "MANUFACTURER";
    public final static String KOLUMNA3 = "ANDROID_VER";
    public final static String KOLUMNA4 = "WWW";
    public final static String TW_BAZY = "CREATE TABLE " + NAZWA_TABELI +
            "("+ID+" integer primary key autoincrement, " +
            KOLUMNA1+" text not null,"+
            KOLUMNA2+" text," +
            KOLUMNA3 + " text," +
            KOLUMNA4 + " text);";
    private static final String KAS_BAZY = "DROP TABLE IF EXISTS "+NAZWA_TABELI;
    private static final String INSERT_PHONE = "INSERT INTO " + NAZWA_TABELI +
            " ("+ID + "," + KOLUMNA1 + "," + KOLUMNA2 + "," + KOLUMNA3 + "," + KOLUMNA4 + ") VALUES (?,?,?,?,?)";
    private static final String UPDATE_PHONE = "UPDATE " + NAZWA_TABELI + " SET MODEL=?, MANUFACTURER=? WHERE ID=?";
    private static final String SELECT_PHONES = "SELECT ID, MODEL, MANUFACTURER, ANDROID_VER, WWW  FROM " + NAZWA_TABELI + " ORDER BY ID ASC";
    private static final String SELECT_PHONES_BY_ID = "SELECT ID, MODEL, MANUFACTURER, ANDROID_VER, WWW  FROM " + NAZWA_TABELI + " WHERE ID=?";
    private static final String DELETE_PHONE = "DELETE FROM " + NAZWA_TABELI + " WHERE ID=?";

    public DBHelper(Context context) {
        super(context,NAZWA_BAZY,null, WERSJA_BAZY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TW_BAZY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KAS_BAZY);
        db.execSQL(TW_BAZY);
    }

    public long insertPhone(ContentValues contentValues) {
        return this
                .getWritableDatabase()
                .insert(NAZWA_TABELI, null, contentValues);
    }

    public void updatePhone(Phone phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(UPDATE_PHONE);

        stmt.bindString(1, phone.getModel());
        stmt.bindString(2, phone.getManufacturer());
        stmt.bindLong(3, phone.getID());

        closeDb(db, stmt);
    }

    public List<Phone> getPhones() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_PHONES, null);
        List<Phone> phones = new ArrayList<>();

        while(cursor.moveToNext()) {
            Phone phone = new Phone(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            phones.add(phone);
        }

        db.close();

        return phones;
    }

    public Phone getPhonesById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_PHONES_BY_ID, new String[]{String.valueOf(id)});

        db.close();

        return new Phone(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));

    }

    public void deletePhone(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(DELETE_PHONE);
        stmt.bindLong(1, id);

        closeDb(db,stmt);
    }


    private void closeDb(SQLiteDatabase db , SQLiteStatement stms) {
        stms.execute();
        stms.close();
        db.close();
    }
}
