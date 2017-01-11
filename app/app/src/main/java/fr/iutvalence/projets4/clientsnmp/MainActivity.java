package fr.iutvalence.projets4.clientsnmp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    private static AppCompatActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.instance = this;

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final MIBDictionary mib = new MIBDictionary();
        Log.i("SysDescr", mib.getMIBElement(MIBDictionary.SYSDESCR_OID).getValue().toString());

        new CountDownTimer(1000*360, 333)
        {

            @Override
            public void onTick(long l) {
                //Log.i("MemoryUsages", mib.getMIBElement(new OID(".1.3.6.1.2.1.1.2")).getValue().toString());
                Log.i("Disk", mib.getMIBElement(MIBDictionary.HWDISKUSAGE).getValue().toString());
            }

            @Override
            public void onFinish() {

            }
        }.start();


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

    public static Context getContext() {
        return MainActivity.instance.getApplicationContext();
    }
}
