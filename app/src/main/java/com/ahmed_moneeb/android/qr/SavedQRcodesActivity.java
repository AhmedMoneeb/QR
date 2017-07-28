package com.ahmed_moneeb.android.qr;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ahmed_moneeb.android.qr.data.QRcodeDbContract;

public class SavedQRcodesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, CodesAdapter.QRcodeClickListener {

    public static final int Loader_id = 2412;
    Toolbar s_toolbar;
    Boolean inTwoPaneMode = false;
    String nameOfClickedItem;
    String contentOfClickedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_qrcodes);

        s_toolbar = (Toolbar) findViewById(R.id.s_toolbar);
        s_toolbar.setTitle(getString(R.string.saved_qr_codes_activity_title));
        setSupportActionBar(s_toolbar);


        if (findViewById(R.id.QRcode_values_not_single_fragment) != null) {
            inTwoPaneMode = true;
        } else {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            QRcodesNamesFragment fragment = new QRcodesNamesFragment();
            transaction.add(R.id.QRcode_names_single_fragment, fragment);
            transaction.commit();
        }

    }

    @Override
    public void onNameClicked(String name) {
        nameOfClickedItem = name;
        getSupportLoaderManager().destroyLoader(Loader_id);
        getSupportLoaderManager().initLoader(Loader_id, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Cursor cur;
                    String selection = QRcodeDbContract.QRcodesTable.COLUMN_CODE_NAME + " = ?";
                    String[] selectionArgs = {nameOfClickedItem};
                    cur = getContext().getContentResolver().query(QRcodeDbContract.QRcodesTable.CODES_URI
                            , new String[]{QRcodeDbContract.QRcodesTable.COLUMN_CODE_CONTENT}
                            , selection, selectionArgs, null);
                    return cur;
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (data != null) {
            if (inTwoPaneMode) {
                if (data.moveToFirst()) {
                    new Handler().post(new Runnable() {
                        public void run() {
                            try {
                                int contentIndex = data.getColumnIndex(QRcodeDbContract.QRcodesTable.COLUMN_CODE_CONTENT);
                                contentOfClickedItem = data.getString(contentIndex);
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                SavedQRcodeFragment fragment = new SavedQRcodeFragment();
                                transaction.replace(R.id.QRcode_values_not_single_fragment, fragment);
                                transaction.commit();
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                    });


                }
            } else {
                if (data.moveToFirst()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int contentIndex = data.getColumnIndex(QRcodeDbContract.QRcodesTable.COLUMN_CODE_CONTENT);
                                contentOfClickedItem = data.getString(contentIndex);
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                SavedQRcodeFragment fragment = new SavedQRcodeFragment();
                                transaction.replace(R.id.QRcode_names_single_fragment, fragment);
                                transaction.commit();
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                    });
                }
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
