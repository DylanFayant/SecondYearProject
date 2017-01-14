package fr.iutvalence.projets4.clientsnmp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import fr.iutvalence.projets4.clientsnmp.MIB.MIBElement;

public class ServiceActivity extends Activity {
    Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        this.intent =new Intent(this, AgentService.class);
        this.intent.setData(Uri.parse("Donnée"));
    }

    public void StartSvc(View v) {
        Log.d("Test","Appuyé sur le bouton start, debug");
        this.startService(this.intent);
    }

    public void StopSvc(View v) {
        stopService(new Intent(this, AgentService.class));
        Log.d("Test","Appuyé sur le bouton stop");
        this.stopService(intent);
    }
}