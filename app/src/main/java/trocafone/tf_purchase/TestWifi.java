package trocafone.tf_purchase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class TestWifi extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testwifi);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        String[] myKeys = getResources().getStringArray(R.array.sections);

        TextView title = findViewById(R.id.title);
        title.setText(myKeys[position]);

        TextView wifiStatus = findViewById(R.id.wifi_status);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Context context = getApplicationContext();
        String wifiStatusText = mWifi.isConnected() ? "on" : "off";
        String wifiText = context.getResources().getString(getResources().getIdentifier("wifi_"+wifiStatusText, "string",
                context.getPackageName()));
        wifiStatus.setText(wifiText);
    }

}