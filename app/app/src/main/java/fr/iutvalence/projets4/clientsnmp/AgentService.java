package fr.iutvalence.projets4.clientsnmp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.security.UsmUser;
import org.snmp4j.security.UsmUserEntry;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;

import java.io.IOException;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.AgentTest.MOCreator;
import fr.iutvalence.projets4.clientsnmp.AgentTest.SNMPAgent;
import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;

import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;
import fr.iutvalence.projets4.clientsnmp.MIB.MIBElement;

import static org.snmp4j.mp.SnmpConstants.sysDescr;

public class AgentService extends IntentService {

    public AgentService(){
        super(AgentService.class.getName());
    }
    private Handler handle= new Handler();
    private static SNMPAgent agent;

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Configuration.setContext(this);
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

        MIBDictionary dic = new MIBDictionary();
        registerManagedObject(agent, dic.getMIBOids(),dic);
        //runNotifier.run();
    }


    /**
     * Register all Managed Objects from our MIB
     * @param agent an snmpv2 agent to register the values onto
     * @param oidList a list of OID with the same baseOID (size-2)
     * @param dic the dictionary where the values are
     */
    private void registerManagedObject(SNMPAgent agent,List<OID> oidList, MIBDictionary dic){

        ////////////////////////////////////////////////////////////////////////////////////////
        MIBElement[][] mergedElements = new MIBElement[OID.MAX_OID_LEN][OID.MAX_OID_LEN];
        OID baseOID=oidList.get(1).trim().trim();
        int i;
        int j;
        int column;
        int line;
        int maxC=1;
        int maxL=1;
        for (i = 0; i<oidList.size(); i++) {
            OID current=oidList.get(i);
            Log.d("current",current.toString());//Debug
            int[] parsedOID = current.toIntArray();
            column = parsedOID[parsedOID.length-2];
            line=parsedOID[parsedOID.length-1];
            mergedElements[column][line]=dic.getMIBElement(current);
            if(maxC<column)maxC=column;
            if(maxL<line)maxL=line;
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        MOTableBuilder builder = new MOTableBuilder(baseOID);
        for(i=1;i<=maxC;i++){
            builder.addColumnType(SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_WRITE);
            for(j=1;j<=maxL;j++){
                if (mergedElements[i][j]== null){
                    builder.addRowValue(new OctetString("No object here"));
                    Log.d("MERGED","NULL item "+i+", "+j);//Debug

                }else{
                    Log.d("MERGED",mergedElements[i][j].getValue().toString());//Debug
                    builder.addRowValue(new OctetString(mergedElements[i][j].getValue().toString()));
                }
            }
        }
        agent.registerManagedObject(builder.build());
    }

    public SNMPAgent getAgent(){
        return AgentService.agent;
    }
}