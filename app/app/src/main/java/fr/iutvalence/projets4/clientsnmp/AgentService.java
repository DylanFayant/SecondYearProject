package fr.iutvalence.projets4.clientsnmp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.VariantVariable;
import org.snmp4j.smi.VariantVariableCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.AgentTest.SNMPAgent;
import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;

import fr.iutvalence.projets4.clientsnmp.MIB.MIBElement;

/**
 * Updated by Simon Foëx on 19/02/2017.
 */
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
        registerManagedObject(dic.getMIBOids(),dic);
        /*runNotifier.run();
         Runnable runNotifier =new Runnable(){
            @Override
            public void run() {
                handle.postDelayed(this, 2000);
                Log.d("Notifier","Agent is running");
            }
        };
        */
    }


    /**
     * Register all Managed Objects from our MIB
     * @param oidList a list of OID with the same baseOID (size-2)
     * @param dic the dictionary where the values are
     */
    private void registerManagedObject(List<OID> oidList, MIBDictionary dic) {

        ////////////////////////////////////////////////////////////////////////////////////////
        OID baseOID;
        int i;
        int column;
        int line;
        Log.d("new version","here");
        HashMap<OID,MOTableCharacteristics> buildable = new HashMap<>();
        for (i = 0; i < oidList.size(); i++) {

            OID current = oidList.get(i);

            baseOID = current.trim().trim();

            if (!buildable.containsKey(baseOID)){
                buildable.put(baseOID, new MOTableCharacteristics(baseOID));
            }

            int[] parsedOID = current.toIntArray();
            column = parsedOID[parsedOID.length - 2];
            line = parsedOID[parsedOID.length - 1];

            MOTableCharacteristics working = buildable.get(baseOID);

            if(working.columnCount<column) working.setColumnCount(column);
            if(working.rowCount<line) working.setRowCount(line);
            MIBElement[][] workingE = working.getElements();
            workingE[column][line]= dic.getMIBElement(current);
            working.setElements(workingE);

            buildable.put(baseOID, working);
        }

        for(OID charac: buildable.keySet()){
            MOTableCharacteristics current = buildable.get(charac);
            buildMOTable(current.getColumnCount(),current.getRowCount(),current.getBaseOID(),current.getElements());
        }
    }
        ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Build and register in the MOTable each element, and setting them to the baseOID.column.line OID
     * if you want to register an element at 1.2.3.4.5.6.7.8.9, base OID should be 1.2.3.4.5.6.7 and the element should be at mergedElements[8][9]
     * @param columnCount number of column in the array
     * @param lineCount number of lines in the array
     * @param baseOID common OID used in all the values as a prefix
     * @param mergedElements Array containing all the elements you want to register, syntax is MIBElement[column][line]
     */
        public void buildMOTable(int columnCount, int lineCount, OID baseOID, MIBElement[][] mergedElements){
            Log.d("MAXC",""+columnCount);
            Log.d("MAXL",""+lineCount);
            MOTableBuilder builder = new MOTableBuilder(baseOID);
            int i;
            int j;
            for(i=1;i<=columnCount;i++){
                builder.addColumnType(SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_WRITE);
            }
            for(i=1;i<=columnCount;i++){
                for(j=1;j<=lineCount;j++){
                    if (mergedElements[i][j]== null){
                        builder.setRowValue(null,i,j);
                    }else{
                        Log.d("MERGED",mergedElements[i][j].getValue().toString());//Debug

                        final MIBElement merged =mergedElements[i][j];
                        VariantVariableCallback cb = new VariantVariableCallback(){
                            @Override
                            public void variableUpdated(VariantVariable variantVariable) {
                                variantVariable.setValue(new OctetString(merged.getValue().toString()));
                            }

                            @Override
                            public void updateVariable(VariantVariable variantVariable) {
                                variantVariable.setValue(new OctetString(merged.getValue().toString()));
                            }
                        };
                        VariantVariable nVar = new VariantVariable(new OctetString("Not updated yet"),cb);
                        builder.setRowValue(nVar,i,j);
                    }
                }
            }
            agent.registerManagedObject(builder.build());
        }
    public SNMPAgent getAgent(){
        return AgentService.agent;
    }
}