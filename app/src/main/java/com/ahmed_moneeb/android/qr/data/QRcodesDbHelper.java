package com.ahmed_moneeb.android.qr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed on 7/20/2017.
 */

public class QRcodesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "QRcodes.db";

    public QRcodesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_CODES_TABLE = "CREATE TABLE " + QRcodeDbContract.QRcodesTable.TABLE_NAME
                + " ( " + QRcodeDbContract.QRcodesTable.COLUMN_CODE_NAME + " TEXT NOT NULL , "
                + QRcodeDbContract.QRcodesTable.COLUMN_CODE_CONTENT + " TEXT NOT NULL "
                + " );";
        db.execSQL(CREATE_CODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QRcodeDbContract.QRcodesTable.TABLE_NAME);
        onCreate(db);
    }
}
