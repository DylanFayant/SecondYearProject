package fr.iutvalence.projets4.clientsnmp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OctetString;

import java.io.IOException;

import fr.iutvalence.projets4.clientsnmp.AgentTest.MOCreator;
import fr.iutvalence.projets4.clientsnmp.AgentTest.SNMPAgent;

import static org.snmp4j.mp.SnmpConstants.sysDescr;

public class AgentService extends IntentService {

    public AgentService(){
        super(AgentService.class.getName());
    }

    Handler handle= new Handler();
    SNMPAgent agent;

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString
        Log.d("Test", "Service lancé");
        Log.d("Test", "Donnée:" + dataString);


        Runnable runNotifier =new Runnable(){
            @Override
            public void run() {
                handle.postDelayed(this, 2000);
                Log.d("Notifier","Agent is running");
            }
        };
        try {
            agent = new SNMPAgent("udp:127.0.0.1/2001");
            agent.getUsm().addUser(new OctetString("MD5DES"),new UsmUser(new OctetString("MD5DES"),null, null, null, null));
            agent.getUsm().getUserTable().addUser(agent.getUsm().getUser(agent.getUsm().getLocalEngineID(),new OctetString("MD5DES")));
            agent.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Since BaseAgent registers some MIBs by default we need to unregister
        // one before we register our own sysDescr. Normally you would
        // override that method and register the MIBs that you need
        agent.unregisterManagedObject(agent.getSnmpv2MIB());

        // Register a system description, use one from you product environment
        // to test with
        agent.registerManagedObject(MOCreator.createReadOnly(sysDescr,
                "Normalement la description système est là"));

        runNotifier.run();


    }
}