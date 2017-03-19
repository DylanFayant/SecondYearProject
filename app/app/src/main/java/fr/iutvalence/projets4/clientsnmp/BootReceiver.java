package fr.iutvalence.projets4.clientsnmp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import fr.iutvalence.projets4.clientsnmp.Agent.AgentService;

/**
 * Created by simon on 17/03/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, AgentService.class);
            context.startService(serviceIntent);
        }
    }
}
