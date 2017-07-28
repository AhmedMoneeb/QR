package com.ahmed_moneeb.android.qr.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ahmed on 7/20/2017.
 */

public final class QRcodeDbContract {

    private QRcodeDbContract() {
    }

    public static class QRcodesTable implements BaseColumns {
        public static final Uri CODES_URI = Uri.parse("content://com.ahmed_moneeb.android.qr/codes");
        public static final String TABLE_NAME = "codes";
        public static final String COLUMN_CODE_NAME = "name";
        public static final String COLUMN_CODE_CONTENT = "content";
    }
}
