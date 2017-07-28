package com.ahmed_moneeb.android.qr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class GeneratingQRcodeActivity extends AppCompatActivity
        implements GeneratingQRcodeFragment.generateButtonClicklistener {

    public Boolean inTwoPaneMode;
    Toolbar toolbar;
    String toBeQRcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating_qrcode);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.generating_qr_code_activity_title));
        setSupportActionBar(toolbar);
        if (findViewById(R.id.generating_fragment) != null) {
            inTwoPaneMode = true;
        } else {
            inTwoPaneMode = false;
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            GeneratingQRcodeFragment fragment = new GeneratingQRcodeFragment();
            transaction.add(R.id.generating, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onGenerateButtonClicked(String text) {
        toBeQRcode = text;

        if (inTwoPaneMode) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            GeneratedQRcodeFragment f = new GeneratedQRcodeFragment();
            transaction.replace(R.id.generated_fragment, f);
            transaction.commit();
        } else {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            GeneratedQRcodeFragment f = new GeneratedQRcodeFragment();
            transaction.replace(R.id.generating, f);
            transaction.commit();
        }


    }
}
