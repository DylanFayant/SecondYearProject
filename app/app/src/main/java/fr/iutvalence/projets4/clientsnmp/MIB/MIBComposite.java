package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

/**
 * A MIBComposite is a composite of the MIB tree (a component who have composites "a node" or an element who have a behavior "a leaf")
 * We can get its sub-composites and set a new sub-composite to this element.
 */
public interface MIBComposite {

    public MIBComposite getComposite(OID oid);
    public void setComposite(OID oid, MIBComposite mibComposite);

}
