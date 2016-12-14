package com.midtrans.sdk.uikit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.utilities.SmsUtils;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

public class PaymentWebActivity extends BaseActivity {
    private Toolbar toolbar;
    private String webUrl;
    private String type = "";

    private SmsVerifyCatcher smsVerifyCatcher;
    private WebviewFragment webviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);

        saveCurrentFragment = true;
        webUrl = getIntent().getStringExtra(Constants.WEBURL);
        if (getIntent().getStringExtra(Constants.TYPE) != null && !getIntent().getStringExtra(Constants.TYPE).equals("")) {
            type = getIntent().getStringExtra(Constants.TYPE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initializeTheme();

        toolbar.setTitle(R.string.processing_payment);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!type.equals("")) {
            webviewFragment = WebviewFragment.newInstance(webUrl, type);
        } else {
            webviewFragment = WebviewFragment.newInstance(webUrl);
        }
        replaceFragment(webviewFragment, R.id.webview_container, true, false);

        if (type != null && type.equalsIgnoreCase(WebviewFragment.TYPE_CREDIT_CARD)) {
            initSmsCatcher();
            /*if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webviewFragment.setOtp("112233");
                    }
                }, 5000);
            } else {
                // Init SMS Catcher
                //initSmsCatcher();
            }*/
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            showCancelConfirmationDialog();
            return true;
        } else if (item.getItemId() == R.id.action_close) {
            showCancelConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showCancelConfirmationDialog();
    }

    private void showCancelConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle(R.string.cancel_transaction)
                .setMessage(R.string.cancel_transaction_message)
                .create();
        dialog.show();
    }

    private void initSmsCatcher() {
        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                Logger.d("Received message: " + message);
                Toast.makeText(PaymentWebActivity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                String code = SmsUtils.getCodeFromMessage(message);
                Logger.d("Received code: " + code);
                Toast.makeText(PaymentWebActivity.this, "Code: " + code, Toast.LENGTH_SHORT).show();
                if (code != null && !TextUtils.isEmpty(code)) {
                    webviewFragment.setOtp(code);
                }
            }
        });

        smsVerifyCatcher.setFilter("CIMB Niaga: Paycode Anda adalah [0-9]+ utk transaksi di [a-zA-Z0-9 ]+ sebesar IDR [0-9.,]+. Paycode berlaku selama [0-9]+ mnt.( Pengiriman [0-9]+ dari [0-9]+.)?|From BCA: Your Authorisation code is [0-9]+ for the purchase at [a-zA-Z0-9 ]+, amount IDR [0-9.,]+. Valid for [0-9]+ mins.( Resend [0-9]+ of [0-9]+.)?|Dari BNI: Kode Otorisasi utk transaksi Anda adalah [0-9]+ di [a-zA-Z0-9 ]+ sebesar IDR [0-9.,]+.Kode Anda berlaku [0-9]+ mnt.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (smsVerifyCatcher != null) {
            smsVerifyCatcher.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (smsVerifyCatcher != null) {
            smsVerifyCatcher.onStop();
        }
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
