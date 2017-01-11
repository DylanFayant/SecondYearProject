package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.ActivityManager;

import com.jaredrummler.android.processes.AndroidProcesses;

import org.snmp4j.smi.OID;

import java.util.List;

import fr.iutvalence.projets4.clientsnmp.MainActivity;

/**
 * Created by ethis on 09/01/17.
 */

public class SrvcTMMOpen implements MIBComposite, MIBElement<Boolean> {

    public final static String TMM_PACKAGE = "fr.iutvalence.fayantd.braintrainer";

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Boolean getValue() {

        List<ActivityManager.RunningAppProcessInfo> processes = AndroidProcesses.getRunningAppProcessInfo(MainActivity.getContext());

        for (int i = 0; i < processes.size(); i++)
        {
            if(processes.get(i).processName.equals(TMM_PACKAGE))
                return true;
        }
        return false;
    }
}
