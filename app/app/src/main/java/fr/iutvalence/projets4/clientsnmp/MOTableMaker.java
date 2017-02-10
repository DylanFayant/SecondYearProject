package fr.iutvalence.projets4.clientsnmp;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableSubIndex;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;

import java.util.ArrayList;
import java.util.List;


public class MOTableMaker {

    private MOTableSubIndex[] subIndexes = new MOTableSubIndex[] { new MOTableSubIndex(
            SMIConstants.SYNTAX_INTEGER) };
    private MOTableIndex indexDef = new MOTableIndex(subIndexes, false);

    private final List<MOColumn> columns = new ArrayList<MOColumn>();
    private final List<Variable[]> tableRows = new ArrayList<Variable[]>();
    private int colCnt =0;
    private OID tableRootOid;
    private static int defSyntax = SMIConstants.SYNTAX_OCTET_STRING;
    private static MOAccess defAccess = MOAccessImpl.ACCESS_READ_WRITE;

    /**
     * Specified oid is the root oid of this table
     */
    public MOTableMaker(OID oid) {
        this.tableRootOid = oid;
    }

    public MOTableMaker addColumn(int syntax, MOAccess access) {
        colCnt++;
        columns.add(new MOColumn(colCnt,syntax, access));
        return this;
    }


    public MOTableMaker addValue(Variable variable, int columnIndex, int rowIndex) {
        try {
            MOColumn column=columns.get(columnIndex);
        }
        catch(IndexOutOfBoundsException e){
            addColumn(defSyntax,defAccess);
            return addValue(variable,columnIndex,rowIndex);
        }
        return this;

    }

    public MOTable build() {
        DefaultMOTable ifTable = new DefaultMOTable(tableRootOid, indexDef,
                columns.toArray(new MOColumn[0]));
        MOMutableTableModel model = (MOMutableTableModel) ifTable.getModel();
        int i = 1;

        for (Variable[] variables : tableRows) {
            model.addRow(new DefaultMOMutableRow2PC(new OID(String.valueOf(i)),
                    variables));
            i++;
        }
        ifTable.setVolatile(true);
        return ifTable;
    }

}
