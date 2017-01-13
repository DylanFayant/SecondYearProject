package fr.iutvalence.projets4.clientsnmp.MIB;

import android.os.Build;

import org.snmp4j.smi.OID;

/**
 * Class who gives us the device's android version
 * Created by ethis on 09/01/17.
 */
public class SysAndroidVersion implements MIBComposite, MIBElement<String> {

    /**
     * Get the current composite
     * It returns this because we are in a tree's leaf
     * @param oid
     * @return MIBComposite (this)
     */
    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    /**
     * Set a composite into the current composite
     * This function is disabled because we can't add sub-tree to a leaf
     * @param oid
     * @param mibComposite
     */
    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    /**
     * Gives the device's android version
     * @return A string with the android release installed into the device
     */
    @Override
    public String getValue() {
        return Build.VERSION.RELEASE;
    }
}
