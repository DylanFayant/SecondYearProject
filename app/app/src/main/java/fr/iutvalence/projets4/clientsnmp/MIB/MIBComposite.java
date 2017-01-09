package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public interface MIBComposite {

    public MIBComposite getComposite(OID oid);
    public void setComposite(OID oid, MIBComposite mibComposite);

}
