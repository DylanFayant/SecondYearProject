package fr.iutvalence.projets4.clientsnmp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import fr.iutvalence.projets4.clientsnmp.AndroidDebugInterface.Adapter;
import fr.iutvalence.projets4.clientsnmp.AndroidDebugInterface.ListServiceToSurvey;
import fr.iutvalence.projets4.clientsnmp.AndroidDebugInterface.ServerUDPListener;
import fr.iutvalence.projets4.clientsnmp.MIB.MIBDictionary;

/**
 * This class launch debug interface.
 * This interface contain :
 *  -data about the device
 *  -service status to monitor
 *  -network activity
 * Created by Thundermist on 13/01/2017.
 */

public class
DebugInterface extends ActionBarActivity {
    TabHost tabHost;
    ListView mListViewDataDevice;
    ListView mListViewService;

    Adapter adapterDeviceData;
    Adapter adapterService;

    String[] DataToUpdate;

    private Timer timer;
    private TimerTask timerService;
    private TimerTask timerDataCPU;
    private TimerTask timerDataOther;

    final Handler handlerService = new Handler();
    final Handler handlerDataCPU = new Handler();
    final Handler handlerDataOther = new Handler();

    final MIBDictionary mib = new MIBDictionary();

    private static DebugInterface instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Configuration.setContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_interface);
        DebugInterface.instance = this;

        //Create two ListView who refer to ListView in the layout debug_interface
        this.mListViewDataDevice = (ListView) findViewById(R.id.listViewDataDevice);
        this.mListViewService = (ListView) findViewById(R.id.listViewService);
        //Fill DataToUpdate with data about the device (Device name,Android version,system up time,CPU usage,disk usage, memory usage and temperature of device)
        this.DataToUpdate = fillData();
        //Fill "adapterDeviceData" and "adapterService" with data previously recovered
        initView();
        //Set adapters corresponding to ListView
        mListViewDataDevice.setAdapter(this.adapterDeviceData);
        mListViewService.setAdapter(this.adapterService);

        //Initialize an item TabHost who contain three tab
        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();


        //Initialize Tab 1 in TabHost and set this name
        TabHost.TabSpec spec = host.newTabSpec("MIB Data");
        spec.setContent(R.id.tab1);
        spec.setIndicator("MIB DATA");
        host.addTab(spec);

        //Initialize Tab 2 in TabHost and set this name
        spec = host.newTabSpec("List Process");
        spec.setContent(R.id.tab2);
        spec.setIndicator("List Process");
        host.addTab(spec);

        //Initialize Tab 3 in TabHost and set this name
        spec = host.newTabSpec("Activité Réseau");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Activité Réseau");
        host.addTab(spec);
        startTimer();
    }

    /**
     * Run a timer who execute three timer task different (Service Status,CPU Usage and Other Data)
     * at three momen different
     * @return void
     */
    public void startTimer() {
        timer = new Timer();
        initializeTimerTaskServiceStatus();
        timer.schedule(timerService, 5000, 1000);
        initializeTimerTaskUpdateCPUData();
        timer.schedule(timerDataCPU, 5000, 3000);
        initializeTimerTaskUpdateOtherData();
        timer.schedule(timerDataOther, 5000, 1000);
    }


    /**
     * Run a timer who update CPU usage in the string list "timerDataCPU"
     * @return void
     */
    public void initializeTimerTaskServiceStatus() {
        timerService = new TimerTask() {
            public void run() {
                handlerService.post(new Runnable() {
                    public void run() {
                        updateServiceStatus();
                    }
                });
            }
        };
    }

    /**
     * Run a new activity who monitor network activity
     * @return void
     */
    public void RunNetworkActivity(View v)
    {
        Intent networkActivity = new Intent(this,ServerUDPListener.class);
        Log.i("pif","pif");
        startActivity(networkActivity);
    }

    /**
     * Initialize timer task who run an handle who update CPU usage in the string list "timerDataCPU"
     * @return void
     */
    public void initializeTimerTaskUpdateCPUData() {
        timerDataCPU = new TimerTask() {
            public void run() {
                handlerDataCPU.post(new Runnable() {
                    public void run() {
                        updateDataCPU();
                    }
                });
            }
        };
    }

    /**
     * Initialize timer task who run an handle who update system up time,disk usage, memory usage and temperature of device
     * in the string list "timerDataOther"
     * @return void
     */
    public void initializeTimerTaskUpdateOtherData(){
        timerDataOther = new TimerTask() {
            public void run() {
                handlerDataOther.post(new Runnable() {
                    public void run() {
                        updateOtherData();
                    }
                });
            }
        };
    }

    /**
     * Transofrm a time in milliseconds into a clear String : %day% d %hours% h %minutes% m %secondes% s
     * @param timeInMilliseconds
     * @return string of clear time
     */
    static private String getClearUptime(long timeInMilliseconds) {
        long seconds = timeInMilliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = days + " d " + hours % 24 + " h " + minutes % 60 + " m " + seconds % 60 + " s";

        return time;
    }

    /**
     * Fill string list with name of data and service who have to monitor
     * Set one adapter with the first string list and set the second adapter with th second string list
     * @return void
     */
    private void initView()
    {
        String[] listInfoToSurvey = {"Systeme description ","Android version ","Sys uptime ","CPU usage ","Disque usage ","Memory usage ","Temperature "};
        ListServiceToSurvey listOfProcess = new ListServiceToSurvey();
        this.adapterDeviceData = new Adapter(Configuration.getContext(),listInfoToSurvey,this.DataToUpdate);
        this.adapterService = new Adapter(Configuration.getContext(),listOfProcess.getListOfProcessToSurvey(),listOfProcess.getProcessStatus());
    }

    /**
     * Update service status in the adapter and refresh the view for display new data
     * @return void
     */
    private void updateServiceStatus()
    {
        ListServiceToSurvey listOfProcess = new ListServiceToSurvey();
        adapterService.update(listOfProcess.getProcessStatus());
        adapterService.notifyDataSetChanged();
    }

    /**
     * Update cpu usage in the adapter and refresh the view for display new data
     * @return void
     */
    private void updateDataCPU()
    {
        this.DataToUpdate[3]=(100 * (double) mib.getMIBElement(MIBDictionary.HWCPUUSAGE).getValue()) + "%";
    }

    /**
     * Update system up time,disk usage, memory usage and temperature of device (if they have a sensor to capture temperature)
     * in the adapter and refresh the view for display new data
     * @return void
     */
    private void updateOtherData()
    {
        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(2);
        this.DataToUpdate[2]=DebugInterface.getClearUptime((long) mib.getMIBElement(MIBDictionary.SYSUPTIME_OID).getValue());
        this.DataToUpdate[4]=(f.format(mib.getMIBElement(MIBDictionary.HWDISKUSAGE).getValue())) + "%";
        this.DataToUpdate[5]=(f.format(mib.getMIBElement(MIBDictionary.HWMEMORYUSAGE).getValue())) + "%";
        this.DataToUpdate[6]=((Long) mib.getMIBElement(MIBDictionary.HWSENSORACTIVITY).getValue())/1000 + "°C";
        adapterDeviceData.update(DataToUpdate);
        adapterDeviceData.notifyDataSetChanged();
    }

    /**
     * Fill in one shot all data who need to monitor (Device name,Android version,system up time,CPU usage,disk usage, memory usage and temperature of device)
     * (This function was heavy for all device, don't abuse them)
     * @return void
     */
    private String[] fillData()
    {

        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(2);
        String[] listDataToSurvey =
                {
                        mib.getMIBElement(MIBDictionary.SYSDESCR_OID).getValue().toString(),
                        mib.getMIBElement(MIBDictionary.SYSANDROIDVERSION_OID).getValue().toString(),
                        DebugInterface.getClearUptime((long) mib.getMIBElement(MIBDictionary.SYSUPTIME_OID).getValue()),
                        (100 * (double) mib.getMIBElement(MIBDictionary.HWCPUUSAGE).getValue()) + "%",
                        (f.format(mib.getMIBElement(MIBDictionary.HWDISKUSAGE).getValue())) + "%",
                        (f.format(mib.getMIBElement(MIBDictionary.HWMEMORYUSAGE).getValue())) + "%",
                        ((Long) mib.getMIBElement(MIBDictionary.HWSENSORACTIVITY).getValue())/1000 + "°C"
                };
        return listDataToSurvey;
    }

}