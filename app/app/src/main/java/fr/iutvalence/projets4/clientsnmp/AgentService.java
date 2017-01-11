package fr.iutvalence.projets4.clientsnmp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class AgentService extends IntentService {

    public AgentService(){
        super(AgentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString
        Log.d("Test", "Service lancé");
        Log.d("Test", "Donnée:" + dataString);
        int i = 1;
        int j;
        while (i < 1000){
            for (j = 1; j < 1000000000; j++) {

            }
            Log.d("Test", "i = "+i);
            i++;
        }
    }
}