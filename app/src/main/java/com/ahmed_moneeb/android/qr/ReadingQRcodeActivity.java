package com.ahmed_moneeb.android.qr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ReadingQRcodeActivity extends AppCompatActivity {

    public static final String QR_READING_RES_EXTRA = "com.ahmed_moneeb.android.qr.readingQRcodeResult";
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_qrcode);
        surfaceView = (SurfaceView) findViewById(R.id.reading_QR_surface_view);
        surfaceView.setSecure(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();
        OpenCam();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedFps(25.0f)
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        finish();
                    }
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        barcodeDetector.setProcessor(new QRProcessor(this));
        Log.i("detector_operational : ", String.valueOf(barcodeDetector.isOperational()));

    }


    public void OpenCam() {
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
        if (barcodeDetector != null) {
            barcodeDetector.release();
            barcodeDetector = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class QRProcessor implements Detector.Processor<Barcode> {

        Context context;

        public QRProcessor(Context context) {
            this.context = context;
        }

        @Override
        public void release() {
            //cameraSource.release();
        }

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            final SparseArray<Barcode> barcodes = detections.getDetectedItems();

            if (barcodes.size() != 0) {
                Intent intent = new Intent(context, ReadingQRcodeResultActivity.class);
                intent.putExtra(QR_READING_RES_EXTRA, barcodes.valueAt(0).displayValue);

                barcodeDetector.release();
                barcodeDetector = null;
                startActivity(intent);
            }
        }

    }
}
