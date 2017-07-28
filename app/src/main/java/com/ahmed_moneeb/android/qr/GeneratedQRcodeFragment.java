package com.ahmed_moneeb.android.qr;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ahmed_moneeb.android.qr.Widget.SavedCodesWidget;
import com.ahmed_moneeb.android.qr.data.QRcodeDbContract;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratedQRcodeFragment extends Fragment {

    ImageView QRcode;
    Button saveButton;

    public GeneratedQRcodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generated_qrcode, container, false);

        Toolbar bar = ((GeneratingQRcodeActivity) getActivity()).toolbar;
        bar.setTitle(getString(R.string.generating_qr_code_activity_second_title));

        saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
            }
        });


        QRcode = (ImageView) view.findViewById(R.id.QRcode_imageView);
        String temp = ((GeneratingQRcodeActivity) getActivity()).toBeQRcode;
        new CreateQRTask(temp).execute();


        return view;

    }


    public void save(View v) {

        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(getString(R.string.DialogTitle));
        final EditText nameEditText = new EditText(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        nameEditText.setLayoutParams(layoutParams);
        builder.setView(nameEditText);

        builder.setPositiveButton(R.string.save_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nameEditText.getText().toString().length() > 0 && nameEditText.getText().toString().length() <= 16) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(QRcodeDbContract.QRcodesTable.COLUMN_CODE_NAME
                            , nameEditText.getText().toString());
                    contentValues.put(QRcodeDbContract.QRcodesTable.COLUMN_CODE_CONTENT,
                            ((GeneratingQRcodeActivity) getActivity()).toBeQRcode);
                    Uri uri = getContext().getContentResolver().insert(QRcodeDbContract.QRcodesTable.CODES_URI,
                            contentValues);

                    if (uri != null) {
                        Context context = getContext().getApplicationContext();
                        ComponentName componentName = new ComponentName(context, SavedCodesWidget.class);
                        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
                        for (int i = 0; i < ids.length; i++) {
                            AppWidgetManager.getInstance(getContext()).notifyAppWidgetViewDataChanged(ids[i], R.id.grid_view);
                        }
                        Toast.makeText(getContext(), getString(R.string.qr_code_saved_successfully), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), getString(R.string.error_saving_qr_code), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.enter_name_warning), Toast.LENGTH_SHORT).show();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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


    class CreateQRTask extends AsyncTask {

        String str;

        public CreateQRTask(String str) {
            this.str = str;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BitMatrix result = null;
            try {
                result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 700, 700, null);
            } catch (IllegalArgumentException iae) {
                return null;
            } catch (WriterException e) {
                e.printStackTrace();
            }

            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bm.setPixels(pixels, 0, width, 0, 0, width, height);
            return bm;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            QRcode.setImageBitmap((Bitmap) o);
        }
    }
}
