package fr.iutvalence.projets4.clientsnmp;



import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.text.DecimalFormat;

import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;

/**
 * Created by simon on 11/01/2017.
 */

public class DebugInterface extends ActionBarActivity {
    TabHost tabHost;
    ListView mListView1;
    ListView mListView2;
    final MIBDictionary mib = new MIBDictionary();

    private static AppCompatActivity instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Configuration.initConfiguration(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_interface);
        DebugInterface.instance = this;

        mListView1 = (ListView) findViewById(R.id.listView1);
        mListView2 = (ListView) findViewById(R.id.listView2);
        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(2);

        String[] listToSurvey = new String[7];
        listToSurvey[0] = "Systeme descritpion : " + mib.getMIBElement(MIBDictionary.SYSDESCR_OID).getValue().toString();
        listToSurvey[1] = "Android version : " + mib.getMIBElement(MIBDictionary.SYSANDROIDVERSION_OID).getValue().toString();
        listToSurvey[2] = "Sys uptime : " + mib.getMIBElement(MIBDictionary.SYSUPTIME_OID).getValue().toString();
        listToSurvey[3] = "CPU usage : " + (100 * (double) mib.getMIBElement(MIBDictionary.HWCPUUSAGE).getValue()) + "%";
        listToSurvey[4] = "Disque usage : " + (100 * Double.parseDouble(f.format(mib.getMIBElement(MIBDictionary.HWDISKUSAGE).getValue()))) + "%";
        listToSurvey[5] = "Memory usage : " + (100 * Double.parseDouble(f.format(mib.getMIBElement(MIBDictionary.HWMEMORYUSAGE).getValue()))) + "%";
        listToSurvey[6] = "Temperature : " + ((Long) mib.getMIBElement(MIBDictionary.HWSENSORACTIVITY).getValue() / 1000) + "°C";

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DebugInterface.getContext(),
                android.R.layout.simple_list_item_1, listToSurvey);

        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(DebugInterface.getContext(),
                //android.R.layout.simple_list_item_1, (String[]) mib.getMIBElement(MIBDictionary.SRVCMUSTBEOPEN).getValue());

        mListView1.setAdapter(adapter1);
        //mListView2.setAdapter(adapter2);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("MIB Data");
        spec.setContent(R.id.tab1);
        spec.setIndicator("MIB DATA");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("List Process");
        spec.setContent(R.id.tab2);
        spec.setIndicator("List Process");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("tab 3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("tab 3");
        host.addTab(spec);
    }

    public void refresh()
    {
        ListView lv = (ListView)findViewById(R.id.listView2);
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                (String[]) mib.getMIBElement(MIBDictionary.SRVCMUSTBEOPEN).getValue());
        lv.setAdapter(myarrayAdapter);
    }

    public static Context getContext() {
        return DebugInterface.instance.getApplicationContext();
    }
}