package trocafone.tf_purchase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.io.File;

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

        fetchIMEI();
        fetchInternalStorage();
        fetchExternalStorage();
        fetchRAM();
        fetchBatteryHealth();
    }

    private void fetchIMEI() {
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

    private void fetchRAM() {
        String ram = getTotalRamMemorySize();
        TextView ramTextView = findViewById(R.id.ram);
        ramTextView.setText("RAM: " + ram);
    }

    private void fetchInternalStorage() {
        String internalStorage = getTotalInternalMemorySize();
        TextView internalStorageTextView = findViewById(R.id.internal_storage);
        internalStorageTextView.setText("Internal Storage: " + internalStorage);
    }

    private void fetchExternalStorage() {
        TextView externalStorageTextView = findViewById(R.id.external_storage);
        if (externalMemoryAvailable()) {
            String externalstorage = getTotalExternalMemorySize();
            externalStorageTextView.setText("External Storage: " + externalstorage);
        } else {
            externalStorageTextView.setText("External Storage: " + getString(R.string.external_storage_failure));
        }
    }


    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getTotalExternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    private static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    private String getTotalRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableKB = mi.totalMem;
        return formatSize(availableKB);
    }

    private static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "GB";
                    size /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    private void fetchBatteryHealth() {
        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TextView textview = findViewById(R.id.battery_health);

                int status = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

                if (status == BatteryManager.BATTERY_HEALTH_COLD) {

                    textview.setText("Battery Health: " + getString(R.string.battery_health_cold));
                }
                if (status == BatteryManager.BATTERY_HEALTH_DEAD) {

                    textview.setText("Battery Health: " + getString(R.string.battery_health_dead));

                }
                if (status == BatteryManager.BATTERY_HEALTH_GOOD) {

                    textview.setText("Battery Health: " + getString(R.string.battery_health_good));

                }
                if (status == BatteryManager.BATTERY_HEALTH_OVERHEAT) {

                    textview.setText("Battery Health: " + getString(R.string.battery_health_overheat));

                }
                if (status == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {

                    textview.setText("Battery Health: " + getString(R.string.battery_health_over_voltage));

                }
                if (status == BatteryManager.BATTERY_HEALTH_UNKNOWN) {

                    textview.setText("Battery Health: " + getString(R.string.battery_health_unknown));

                }
                if (status == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {

                    textview.setText("Battery Health: " + getString(R.string.battery_health_unspecified_failure));

                }


            }
        };

        this.registerReceiver(broadcastreceiver, intentfilter);
    }
}