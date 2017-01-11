package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.ActivityManager;

import org.snmp4j.smi.OID;

import fr.iutvalence.projets4.clientsnmp.MainActivity;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by ethis on 09/01/17.
 */

public class SrvcTMMOpen implements MIBComposite, MIBElement<String> {

    public final static String TMM_PACKAGE = "fr.iutvalence.fayantd.braintrainer";

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public String getValue() {
        ActivityManager am = (ActivityManager) MainActivity.getContext().getSystemService(ACTIVITY_SERVICE);
        // The first in the list of RunningTasks is always the foreground task.
        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
        return foregroundTaskPackageName;
    }
}
