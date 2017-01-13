package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.ActivityManager;

import org.snmp4j.smi.OID;

import java.util.Arrays;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.Configuration;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by ethis on 09/01/17.
 */

/**
 * Deprecated from Android Lollipop
 */
public class SrvcForegroundTMM implements MIBComposite, MIBElement<Boolean> {

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Boolean getValue() {
        ActivityManager am = (ActivityManager) Configuration.getContext().getSystemService(ACTIVITY_SERVICE);
        // The first in the list of RunningTasks is always the foreground task.
        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();

        String servicesToWatch = Configuration.getConfigValue("srvc_watch");
        List<String> listOfProcessesToWatch = Arrays.asList(servicesToWatch.split(";"));

        if(listOfProcessesToWatch.contains(foregroundTaskPackageName))
            return true;
        return false;
    }
}
