package fr.iutvalence.projets4.clientsnmp;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.snmp4j.smi.OID;

import java.io.IOException;

import fr.iutvalence.projets4.clientsnmp.AgentTest.SNMPAgent;
import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;

public class MainActivity extends AppCompatActivity {

    private Manager manager = null;
    private SNMPAgent agent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        MIBDictionary mib = new MIBDictionary();
        Log.i("Valeur de MIB", mib.getMIBElement(new OID("1.3.6.1.2.1.1.1")).getValue().toString());
    }

    protected void start(View view) {
        this.manager = new Manager("udp:127.0.0.1/1111");
        String sysDescr = null;
        try {
            sysDescr = manager.getAsString(new OID(".1.3.6.1.2.1.1.1.0"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sysDescr);
    }
    protected void startAgent(View view){
        try {
            agent = new SNMPAgent("udp:127.0.0.1/1111");
            agent.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void stopAgent(View view){

    }
}
