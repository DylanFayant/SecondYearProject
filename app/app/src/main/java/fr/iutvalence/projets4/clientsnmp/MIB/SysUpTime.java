package fr.iutvalence.projets4.clientsnmp.MIB;

import android.os.SystemClock;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public class SysUpTime implements MIBComposite, MIBElement<Long> {

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Long getValue() {
        return SystemClock.uptimeMillis();
    }
}
