package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public class MIBDictionary {

    public final static OID SYSDESCR_OID =          new OID("1.3.6.1.2.1.1.1");
    public final static OID MEMORYUSAGE_OID =       new OID("1.3.6.1.2.1.1.2");
    public final static OID DISKUSAGE_OID =         new OID("1.3.6.1.2.1.1.3");
    public final static OID CPUUSAGE_OID =          new OID("1.3.6.1.2.1.1.4");
    public final static OID SENSOR_ACTIVITY_OID =   new OID("1.3.6.1.2.1.1.5");

    MIBComposite mibTree;

    public MIBDictionary(){
        this.mibTree = new MIBComponent();
        this.setLeafs();
    }

    public void setLeafs() {
        this.mibTree.setComposite(SYSDESCR_OID, new SysDescr());
        this.mibTree.setComposite(MEMORYUSAGE_OID, new MemoryUsage());
        this.mibTree.setComposite(DISKUSAGE_OID, new DiskUsage());
        this.mibTree.setComposite(CPUUSAGE_OID, new Cpu());
        this.mibTree.setComposite(SENSOR_ACTIVITY_OID, new SensorActivity());
    }

    public MIBElement getMIBElement(OID oid)
    {
        return (MIBElement)this.mibTree.getComposite(oid);
    }
}
