package com.example.simon.essaiservice;


import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

public class MainActivity extends Activity {

    public class MyReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "com.myapp.intent.action.TEXT_TO_DISPLAY";

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(MonPremierService.SOURCE_URL);
            // send text to display
            TextView result = (TextView) findViewById(R.id.text_result);
            result.setText(text);

        }
    }

    private MyReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // on initialise notre broadcast
        receiver = new MyReceiver();
        // on lance le service
        Intent msgIntent = new Intent(this, MonPremierService.class);
        msgIntent.putExtra(MonPremierService.URL, "http://api.openweathermap.org/data/2.5/weather?q=London&mode=xml");
        startService(msgIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // on déclare notre Broadcast Receiver
        IntentFilter filter = new IntentFilter(MyReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // on désenregistre notre broadcast
        unregisterReceiver(receiver);
    }
}