package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.ActivityManager;

import com.jaredrummler.android.processes.AndroidProcesses;

import org.snmp4j.smi.OID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.Configuration;

/**
 * Created by ethis on 09/01/17.
 */

/**
 * Deprecated from Android Nougat
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

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = AndroidProcesses.getRunningAppProcessInfo(Configuration.getContext());
        List<String> runningAppProcessesNames = new ArrayList<String>();

        for(ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses)
        {
            runningAppProcessesNames.add(runningAppProcess.processName);
        }

        String servicesToWatch = Configuration.getConfigValue("srvc_watch");
        List<String> listOfProcessesToWatch = Arrays.asList(servicesToWatch.split(";"));

        for(String processToWatch : listOfProcessesToWatch)
        {
            if(!runningAppProcessesNames.contains(processToWatch))
                return false;
        }

        return true;
    }
}
