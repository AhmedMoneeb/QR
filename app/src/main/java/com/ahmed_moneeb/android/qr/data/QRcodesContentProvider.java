package com.ahmed_moneeb.android.qr.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Ahmed on 7/20/2017.
 */

public class QRcodesContentProvider extends ContentProvider {

    public static final int CODES_MATCH = 400;
    public static final int CODES_WITH_ID_MATCH = 401;
    public static final UriMatcher matcher = buildUriMantcher();
    private QRcodesDbHelper DbHelper;

    public static UriMatcher buildUriMantcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("com.ahmed_moneeb.android.qr", QRcodeDbContract.QRcodesTable.TABLE_NAME, CODES_MATCH);
        matcher.addURI("com.ahmed_moneeb.android.qr", QRcodeDbContract.QRcodesTable.TABLE_NAME + "/#", CODES_WITH_ID_MATCH);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DbHelper = new QRcodesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = DbHelper.getReadableDatabase();

        int match = matcher.match(uri);

        Cursor cursor;
        switch (match) {
            case CODES_MATCH:
                cursor = db.query(QRcodeDbContract.QRcodesTable.TABLE_NAME
                        , projection
                        , selection, selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("wrong URI");
        }
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri toBeReturned = null;
        try {
            SQLiteDatabase db = DbHelper.getWritableDatabase();
            int match = matcher.match(uri);

            switch (match) {
                case CODES_MATCH:
                    long id = db.insert(QRcodeDbContract.QRcodesTable.TABLE_NAME, null, values);
                    if (id < 0) {
                    } else {
                        toBeReturned = ContentUris.withAppendedId(uri, id);
                        return toBeReturned;
                    }
                    break;
                default:
            }
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return toBeReturned;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
