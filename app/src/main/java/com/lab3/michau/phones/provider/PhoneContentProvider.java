package com.lab3.michau.phones.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lab3.michau.phones.helper.DBHelper;


public class PhoneContentProvider extends ContentProvider {
    private DBHelper dbHelper;
    public static final String IDENTYFIKATOR = "com.lab3.michau.phones.provider.PhoneContentProvider";
    public static final Uri URI = Uri.parse("content://" + IDENTYFIKATOR + "/" + DBHelper.NAZWA_TABELI);
    public static final int CALA_TABELA = 1;
    public static final int WYBRANY_WIERSZ = 2;
    private static final UriMatcher sDopasowanieUri =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sDopasowanieUri.addURI(IDENTYFIKATOR, DBHelper.NAZWA_TABELI,
                CALA_TABELA);
        sDopasowanieUri.addURI(IDENTYFIKATOR, DBHelper.NAZWA_TABELI +
                "/#",WYBRANY_WIERSZ);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHelper.NAZWA_TABELI);

        int uriType = sDopasowanieUri.match(uri);

        switch (uriType) {
            case WYBRANY_WIERSZ:
                queryBuilder.appendWhere(DBHelper.ID + "="
                        + uri.getLastPathSegment());
                break;
            case CALA_TABELA:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int typUri = sDopasowanieUri.match(uri);
        long id;
        if(typUri == CALA_TABELA) {
            id = dbHelper.insertPhone(contentValues);
        } else {
            throw new IllegalArgumentException("Nie znane uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DBHelper.NAZWA_TABELI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sDopasowanieUri.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case CALA_TABELA:
                rowsDeleted = sqlDB.delete(DBHelper.NAZWA_TABELI,
                        selection,
                        selectionArgs);
                break;

                case WYBRANY_WIERSZ:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DBHelper.NAZWA_TABELI,
                            DBHelper.ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DBHelper.NAZWA_TABELI,
                            DBHelper.ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sDopasowanieUri.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case CALA_TABELA:
                rowsUpdated = sqlDB.update(DBHelper.NAZWA_TABELI,
                        values,
                        selection,
                        selectionArgs);
                break;
            case WYBRANY_WIERSZ:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(DBHelper.NAZWA_TABELI,
                                    values,
                                    DBHelper.ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(DBHelper.NAZWA_TABELI,
                                    values,
                                    DBHelper.ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
