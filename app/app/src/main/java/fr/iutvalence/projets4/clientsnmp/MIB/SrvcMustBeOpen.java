package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.ActivityManager;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import org.snmp4j.smi.OID;

import java.util.Arrays;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.Configuration;

/**
 * Created by ethis on 09/01/17.
 */

public class SrvcMustBeOpen implements MIBComposite, MIBElement<Boolean> {

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Boolean getValue() {
        /*
         * FOREGROUND
         */
        List<AndroidAppProcess> processForeground = AndroidProcesses.getRunningForegroundApps(Configuration.getContext());
        Log.e("Foreground", processForeground.get(0).getPackageName());

        List<ActivityManager.RunningAppProcessInfo> processes = AndroidProcesses.getRunningAppProcessInfo(Configuration.getContext());

        String servicesToWatch = Configuration.getConfigValue("srvc_watch");
        List<String> listOfProcessesToWatch = Arrays.asList(servicesToWatch.split(";"));

        int i = 0;

        for(String aService : listOfProcessesToWatch) {
            for (ActivityManager.RunningAppProcessInfo aProcess : processes)
            {
                if (aProcess.processName.equals(aService)) {
                    i++;
                }
            }
        }

        if(listOfProcessesToWatch.size() == i)
            return true;
        else
            return false;
    }
}
