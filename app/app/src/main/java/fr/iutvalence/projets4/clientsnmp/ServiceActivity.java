package fr.iutvalence.projets4.clientsnmp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ServiceActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void StartSvc(View v) {
        Intent i=new Intent(this, AgentService.class);

        i.putExtra(AgentService.EXTRA_PLAYLIST, "activity_service");
        i.putExtra(AgentService.EXTRA_SHUFFLE, true);
        Log.d("Test:","Entré dans startsvc");
        startService(i);
    }

    public void StopSvc(View v) {
        stopService(new Intent(this, AgentService.class));
    }
}