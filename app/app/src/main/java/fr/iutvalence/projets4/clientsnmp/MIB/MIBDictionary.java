package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public class MIBDictionary {

    public final static OID SYSDESCR_OID =              new OID("1.3.6.1.2.1.1.1");
    public final static OID SYSANDROIDVERSION_OID =     new OID("1.3.6.1.2.1.1.2");
    public final static OID SYSUPTIME_OID =             new OID("1.3.6.1.2.1.1.3");
    public final static OID HWCPUUSAGE =                new OID("1.3.6.1.2.1.2.1");
    public final static OID HWDISKUSAGE =               new OID("1.3.6.1.2.1.2.2");
    public final static OID HWMEMORYUSAGE =             new OID("1.3.6.1.2.1.2.3");
    public final static OID HWSENSORACTIVITY =          new OID("1.3.6.1.2.1.2.4");
    public final static OID SRVCMUSTBEOPEN =            new OID("1.3.6.1.2.1.3.1");

    MIBComposite mibTree;

    public MIBDictionary(){
        this.mibTree = new MIBComponent();
        this.setLeafs();
    }

    public void setLeafs() {
        this.mibTree.setComposite(SYSDESCR_OID,             new SysDescr());
        this.mibTree.setComposite(SYSANDROIDVERSION_OID,    new SysAndroidVersion());
        this.mibTree.setComposite(SYSUPTIME_OID,            new SysUpTime());
        this.mibTree.setComposite(HWCPUUSAGE,               new HwCpuUsage());
        this.mibTree.setComposite(HWDISKUSAGE,              new HwDiskUsage());
        this.mibTree.setComposite(HWMEMORYUSAGE,            new HwMemoryUsage());
        this.mibTree.setComposite(HWSENSORACTIVITY,         new HwSensorActivity());
        this.mibTree.setComposite(SRVCMUSTBEOPEN,           new SrvcMustBeOpen());
    }

    public MIBElement getMIBElement(OID oid)
    {
        return (MIBElement)this.mibTree.getComposite(oid);
    }
}
