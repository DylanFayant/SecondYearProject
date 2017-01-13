package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.ActivityManager;

import com.jaredrummler.android.processes.AndroidProcesses;

import org.snmp4j.smi.OID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.Configuration;

/**
 * Class who checks if every TMM services are open
 * PROBLEM : This method is deprecated in Android Nougat (API 24) for security reasons and not replaced
 * LIBRARY USED : https://github.com/jaredrummler/AndroidProcesses
 * Created by ethis on 09/01/17.
 */
public class SrvcMustBeOpen implements MIBComposite, MIBElement<Boolean> {

    /**
     * Get the current composite
     * It returns this because we are in a tree's leaf
     * @param oid
     * @return MIBComposite (this)
     */
    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    /**
     * Set a composite into the current composite
     * This function is disabled because we can't add sub-tree to a leaf
     * @param oid
     * @param mibComposite
     */
    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    /**
     * Checks if every TMM services are open
     * @return A Boolean true if every TMM services are open false if not
     */
    @Override
    public Boolean getValue() {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = AndroidProcesses.getRunningAppProcessInfo(Configuration.getContext());

        // Create a list of every running processes names
        List<String> runningAppProcessesNames = new ArrayList<String>();
        for(ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses)
        {
            runningAppProcessesNames.add(runningAppProcess.processName);
        }

        // Use the configuration file (res/raw/config.properties) to find every services who must be open
        String servicesToWatch = Configuration.getConfigValue("srvc_must_be_open");
        List<String> listOfProcessesToWatch = Arrays.asList(servicesToWatch.split(";"));

        // If a process to watch is not in the list of processes to watch return false
        for(String processToWatch : listOfProcessesToWatch)
        {
            if(!runningAppProcessesNames.contains(processToWatch))
                return false;
        }

        return true;
    }
}
