package fr.iutvalence.projets4.clientsnmp.MO;

import org.snmp4j.smi.OID;

import fr.iutvalence.projets4.clientsnmp.MIB.MIBElement;

/**
 * Created by Simon Foëx on 19/02/2017.
 */

public class MOTableCharacteristics {
    int columnCount;
    int rowCount;
    OID baseOID;
    MIBElement[][] elements;

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public OID getBaseOID() {
        return baseOID;
    }

    public MIBElement[][] getElements() {
        return elements;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setElements(MIBElement[][] elements) {
        this.elements = elements;
    }

    public MOTableCharacteristics(OID baseOID){
        this.columnCount=1;
        this.rowCount=1;
        this.baseOID=baseOID;
        this.elements=new MIBElement[OID.MAX_OID_LEN][OID.MAX_OID_LEN];
    }
}
