package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public class MIBDictionary {

    MIBComposite mibTree;

    public MIBDictionary(){
        this.mibTree = new MIBComponent();
        this.setLeafs();
    }

    public void setLeafs() {
        this.mibTree.setComposite(new OID("1.3.6.1.2.1.1.1"), new SysDescr());
    }

    public MIBElement getMIBElement(OID oid)
    {
        return (MIBElement)this.mibTree.getComposite(oid);
    }
}
