package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.ActivityManager;

import org.snmp4j.smi.OID;

import java.util.Arrays;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.Configuration;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Class who checks if there is a TMM app in the foreground
 * PROBLEM : This method is deprecated in Android Lollipop (API 21) for security reasons and not replaced
 * Created by ethis on 09/01/17.
 */
public class SrvcForegroundTMM implements MIBComposite, MIBElement<Boolean> {

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
     * Checks if there is a TMM app in the foreground
     * @return A Boolean true if there is a TMM app in the foreground false if not
     */
    @Override
    public Boolean getValue() {
        ActivityManager am = (ActivityManager) Configuration.getContext().getSystemService(ACTIVITY_SERVICE);
        // The first in the list of RunningTasks is always the foreground task.
        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();

        // Use the configuration file (res/raw/config.properties) to find all the services foreground.
        String servicesToWatch = Configuration.getConfigValue("srvc_foreground");
        List<String> listOfProcessesToWatch = Arrays.asList(servicesToWatch.split(";"));

        if(listOfProcessesToWatch.contains(foregroundTaskPackageName))
            return true;
        return false;
    }
}
