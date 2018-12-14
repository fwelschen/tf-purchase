package trocafone.tf_purchase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.TextView;


public class TestPhoneInfo extends Activity {
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testphoneinfo);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        String[] myKeys = getResources().getStringArray(R.array.sections);

        TextView title = findViewById(R.id.title);
        title.setText(myKeys[position]);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            this.setIMEIText(getIMEIFailureText());
        } else {
            this.setIMEIText(getIMEI());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        this.setIMEIText(getIMEIFailureText());
                    } else {
                        this.setIMEIText(getIMEI());
                    }
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getIMEI() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception ex) {
            return getIMEIFailureText();
        }
    }

    private String getIMEIFailureText() {
        return getString(R.string.imei_failure);
    }

    private void setIMEIText(String imeiText) {
        TextView imeiTextView = findViewById(R.id.imei);
        imeiTextView.setText("IMEI: " + imeiText);
    }
}