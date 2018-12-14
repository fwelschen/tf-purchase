package trocafone.tf_purchase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class TestGps extends Activity {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 998;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testgps);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        String[] myKeys = getResources().getStringArray(R.array.sections);

        TextView title = findViewById(R.id.title);
        title.setText(myKeys[position]);

        testGPS();
    }
    private void testGPS() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            this.setGPSTextFailure();
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerNetwork);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        this.setGPSTextFailure();
                    } else {
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerNetwork);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setGPSTextFailure() {
        TextView gpsStatus = findViewById(R.id.gps_status);
        TextView gpsLocation = findViewById(R.id.gps_loaction);

        gpsStatus.setText("GPS: " + getString(R.string.gps_off));
        gpsLocation.setText("GPS location: " + getString(R.string.gps_unavailable_location));
    }

    private void setGPSText(String gpsLocationText) {
        TextView gpsStatus = findViewById(R.id.gps_status);
        TextView gpsLocation = findViewById(R.id.gps_loaction);

        gpsStatus.setText("GPS: " + getString(R.string.gps_on));
        gpsLocation.setText("GPS location: " + gpsLocationText);
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            final double longitudeNetwork = location.getLongitude();
            final double latitudeNetwork = location.getLatitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setGPSText("Location: " + longitudeNetwork + " - " + latitudeNetwork);
                }
            });
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };

}