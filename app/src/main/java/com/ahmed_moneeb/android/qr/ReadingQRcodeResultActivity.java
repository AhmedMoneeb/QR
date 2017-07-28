package com.ahmed_moneeb.android.qr;

import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed_moneeb.android.qr.Widget.SavedCodesWidget;
import com.ahmed_moneeb.android.qr.data.QRcodeDbContract;

import static com.ahmed_moneeb.android.qr.ReadingQRcodeActivity.QR_READING_RES_EXTRA;

public class ReadingQRcodeResultActivity extends AppCompatActivity {

    TextView text;
    Button saveButton;
    String readData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_qrcode_result);
        text = (TextView) findViewById(R.id.text_id);
        saveButton = (Button) findViewById(R.id.save_read_qr_code);
        Intent intent = getIntent();
        readData = intent.getStringExtra(QR_READING_RES_EXTRA);
        text.setText(readData);
    }

    public void searchOnline(View v) {
        Uri uri = Uri.parse("https://www.google.com/#q=" + text.getText().toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void saveReadQR(View v) {
        final AlertDialog.Builder builder;
        final Context context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(getString(R.string.DialogTitle));
        final EditText nameEditText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        nameEditText.setLayoutParams(layoutParams);
        builder.setView(nameEditText);

        builder.setPositiveButton(R.string.save_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nameEditText.getText().toString().length() > 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(QRcodeDbContract.QRcodesTable.COLUMN_CODE_NAME
                            , nameEditText.getText().toString());
                    contentValues.put(QRcodeDbContract.QRcodesTable.COLUMN_CODE_CONTENT, readData);
                    Uri uri = getContentResolver().insert(QRcodeDbContract.QRcodesTable.CODES_URI,
                            contentValues);

                    if (uri != null) {
                        Context context = getApplicationContext();
                        ComponentName componentName = new ComponentName(context, SavedCodesWidget.class);
                        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
                        for (int i = 0; i < ids.length; i++) {
                            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(ids[i], R.id.grid_view);
                        }
                        Toast.makeText(context, getString(R.string.qr_code_saved_successfully), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, getString(R.string.error_saving_qr_code), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.enter_name_warning), Toast.LENGTH_SHORT).show();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);

            }
        });
        builder.setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
