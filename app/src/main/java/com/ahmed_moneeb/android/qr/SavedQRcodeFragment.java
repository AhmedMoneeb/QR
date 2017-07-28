package com.ahmed_moneeb.android.qr;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedQRcodeFragment extends Fragment {


    ImageView savedQRCodeImageView;

    public SavedQRcodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_qrcode, container, false);
        savedQRCodeImageView = (ImageView) view.findViewById(R.id.saved_QR_code_iamge_view);
        String content = ((SavedQRcodesActivity) getActivity()).contentOfClickedItem;
        new CreateQRTask(content).execute();
        return view;
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
            savedQRCodeImageView.setImageBitmap((Bitmap) o);
        }
    }

}
