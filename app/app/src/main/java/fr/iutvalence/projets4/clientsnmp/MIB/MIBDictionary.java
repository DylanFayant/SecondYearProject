package fr.iutvalence.projets4.clientsnmp.MIB;

import android.util.Log;

import org.snmp4j.smi.OID;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the dictionary of the MIB
 * It creates and fill the MIB
 * It allows the access to a MIBElement.
 * Created by ethis on 09/01/17.
 */
public class MIBDictionary {

    /**
     * Description of the MIBElements' OIDs
     */
    public final static OID SYSDESCR_OID =              new OID("1.3.6.1.2.1.1.1");
    public final static OID SYSANDROIDVERSION_OID =     new OID("1.3.6.1.2.1.1.2");
    public final static OID SYSUPTIME_OID =             new OID("1.3.6.1.2.1.1.3");
    public final static OID HWCPUUSAGE =                new OID("1.3.6.1.2.1.2.1");
    public final static OID HWDISKUSAGE =               new OID("1.3.6.1.2.1.2.2");
    public final static OID HWMEMORYUSAGE =             new OID("1.3.6.1.2.1.2.3");
    public final static OID HWSENSORACTIVITY =          new OID("1.3.6.1.2.1.2.4");
    public final static OID SRVCMUSTBEOPEN =            new OID("1.3.6.1.2.1.3.1");
    public final static OID SRVCFOREGROUNDTMM =         new OID("1.3.6.1.2.1.3.2");

    /**
     * The MIB tree's root
     */
    MIBComposite mibTree;

    /**
     * Creates an insance of the MIBDictionary
     *      Creates the MIBTree (its root and set its leafs)
     */
    public MIBDictionary(){
        this.mibTree = new MIBComponent();
        this.setLeafs();
    }

    /**
     * Set each MIB's leafs by calling the setComposite method to the tree's root
     */
    public void setLeafs() {
        this.mibTree.setComposite(SYSDESCR_OID,             new SysDescr());
        this.mibTree.setComposite(SYSANDROIDVERSION_OID,    new SysAndroidVersion());
        this.mibTree.setComposite(SYSUPTIME_OID,            new SysUpTime());
        this.mibTree.setComposite(HWCPUUSAGE,               new HwCpuUsage());
        this.mibTree.setComposite(HWDISKUSAGE,              new HwDiskUsage());
        this.mibTree.setComposite(HWMEMORYUSAGE,            new HwMemoryUsage());
        this.mibTree.setComposite(HWSENSORACTIVITY,         new HwSensorActivity());
        this.mibTree.setComposite(SRVCMUSTBEOPEN,           new SrvcMustBeOpen());
        this.mibTree.setComposite(SRVCFOREGROUNDTMM,        new SrvcForegroundTMM());
    }

    /**
     * Get the OIDs list from the MIB.
     * @return an array of oids.
     */
    public List<OID> getMIBOids() {
        Field[] fields = MIBDictionary.class.getDeclaredFields();

        List oidList = new ArrayList<OID>();
        for (Field f : fields) {
            if(f.getType().equals(OID.class)) {
                try {
                    oidList.add((OID) f.get(new OID()));
                } catch (IllegalAccessException e) {
                    Log.e("MIBDictionary getOIDs", "Illegal Access Exception during getting all the oids");
                }
            }
        }

        return oidList;
    }

    /**
     * Get an MIBElement into the MIBTree.
     * @param oid
     * @return MIBElement (the leaf) or null if there is not leaf in the given path.
     */
    public MIBElement getMIBElement(OID oid)
    {
        return (MIBElement)this.mibTree.getComposite(oid);
    }

}
