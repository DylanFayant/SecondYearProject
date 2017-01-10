package fr.iutvalence.projets4.clientsnmp.MIB;

import android.os.Build;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public class SysDescr implements MIBComposite, MIBElement<String> {

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public String getValue() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }
}
