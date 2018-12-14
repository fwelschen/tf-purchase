package trocafone.tf_purchase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = findViewById(R.id.mainList);
        listview.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(MainActivity.this, TestWifi.class);
                break;
            case 1:
                intent = new Intent(MainActivity.this, TestWifi.class);
                break;
            default:
                intent = new Intent(MainActivity.this, TestWifi.class);

        }
        intent.putExtra("position", position);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
