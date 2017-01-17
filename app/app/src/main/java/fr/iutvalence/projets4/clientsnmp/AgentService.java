package fr.iutvalence.projets4.clientsnmp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.security.UsmUser;
import org.snmp4j.security.UsmUserEntry;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;

import java.io.IOException;

import fr.iutvalence.projets4.clientsnmp.AgentTest.MOCreator;
import fr.iutvalence.projets4.clientsnmp.AgentTest.SNMPAgent;
import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;

import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;
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
        //agent.registerManagedObject(MOCreator.createReadOnly(sysDescr,"Normalement la description système est là"));
        MIBDictionary dic = new MIBDictionary();
        //agent.registerManagedObject(MOCreator.createReadOnly(MIBDictionary.SYSDESCR_OID, dic.getMIBElement(MIBDictionary.SYSDESCR_OID).getValue().toString()));
        //agent.registerManagedObject(MOCreator.createReadOnly(MIBDictionary.SYSUPTIME_OID, dic.getMIBElement(MIBDictionary.SYSUPTIME_OID).getValue().toString()));

        MOTableBuilder builder = new MOTableBuilder(MIBDictionary.SYSDESCR_OID)
                .addColumnType(SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_WRITE)
                //first row
                .addRowValue(new OctetString("loopback"))
                //next row
                .addRowValue(new Integer32(4));
        agent.registerManagedObject(builder.build());

        runNotifier.run();


    }
}