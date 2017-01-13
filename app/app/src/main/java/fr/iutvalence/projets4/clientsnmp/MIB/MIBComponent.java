package fr.iutvalence.projets4.clientsnmp.MIB;

import android.support.annotation.NonNull;

import org.snmp4j.smi.OID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ethis on 09/01/17.
 */

/**
 * A MIBComponent is a node of the MIB tree.
 * It don't have behavior except having sub-composites and setting new sub-composites.
 */
public class MIBComponent implements MIBComposite {

    /**
     * Map of the sub-composites of the current node of the MIB tree.
     */
    Map<Integer, MIBComposite> mibComposites;

    /**
     * Initiate the hashmap of the sub-composites
     */
    public MIBComponent() {
        this.mibComposites = new HashMap<Integer, MIBComposite>();
    }

    /**
     * Get a sub-composite of the tree's node
     * It picks the first id of the given OID and checks if there is a sub-composite in the component's hashmap with this id.
     * If there is a sub-composite with this id, returns the result of the sub-composite's getComposite method with the oid without the first id.
     * Else returns false
     * @param oid the OID of the tree's element (leaf)
     * @return the leaf (MIBComposite) or null if there is not leaf with the given OID
     */
    @Override
    public MIBComposite getComposite(OID oid) {
        int[] oidIntArray = oid.toIntArray();
        OID oidWithoutFirstId = this.getOidWithoutFirstId(oidIntArray);

        MIBComposite sonMibComposite = this.mibComposites.get(oidIntArray[0]);

        if(sonMibComposite == null)
            return null;
        else
            return sonMibComposite.getComposite(oidWithoutFirstId);

    }

    /**
     * Add a the given MIBComposite into the tree at the adress given in the OID.
     * If we are in the last node before the leaf (MIBElement) we add the given MIBComposite to the MAP at the given given oid position
     * Else we verify if the next oid exists in the MAP (if it's not true we add the component into the map at the first id in the oid)
     *      we call the setComposite method with the oid without the first id to the sub-element of the MAP at the first id in the oid.
     * @param oid
     * @param mibComposite
     */
    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {
        int[] oidIntArray = oid.toIntArray();

        if(this.oidIsTheLast(oidIntArray))
        {
            this.mibComposites.put(oidIntArray[0], mibComposite);
        }
        else
        {
            MIBComposite sonMibComposite = this.mibComposites.get(oidIntArray[0]);
            if(sonMibComposite == null)
            {
                this.addComponent(oidIntArray[0]);
                sonMibComposite = this.mibComposites.get(oidIntArray[0]);
            }

            OID oidWithoutFirstId = this.getOidWithoutFirstId(oidIntArray);
            sonMibComposite.setComposite(oidWithoutFirstId, mibComposite);
        }
    }

    /**
     * Creates a news MIBComponent and add it into the MIBComposites' map with the given id
     * @param componentId
     */
    private void addComponent(int componentId) {
        this.mibComposites.put(componentId, new MIBComponent());
    }

    /**
     * Returns the given OID without the first id.
     * @param oidIntArray
     * @return OID
     */
    @NonNull
    private OID getOidWithoutFirstId(int[] oidIntArray) {
        return new OID(Arrays.copyOfRange(oidIntArray, 1, oidIntArray.length));
    }

    /**
     * Check if the oid have only one id (we are in the last node before the leaf)
     * @param oidIntArray
     * @return boolean
     */
    private boolean oidIsTheLast(int[] oidIntArray) {
        return oidIntArray.length == 1;
    }
}
