package fr.iutvalence.projets4.clientsnmp.MO;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.DefaultMOMutableRow2PC;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableIndex;
import org.snmp4j.agent.mo.MOTableSubIndex;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;


/**
 * Based on: http://www.jayway.com/2010/05/21/introduction-to-snmp4j/
 * Utility class to make MOTable objects
 *
 * Author:Simon Foëx
 */
public class MOTableBuilder {

    private MOTableSubIndex[] subIndexes;
    private MOTableIndex indexDef;
    private final List<MOColumn> columns = new ArrayList<MOColumn>();
    private final List<Variable[]> tableRows = new ArrayList<Variable[]>();
    private OID tableRootOid;
    private int colTypeCnt;

    public MOTableBuilder(){
        subIndexes = new MOTableSubIndex[] { new MOTableSubIndex(
                SMIConstants.SYNTAX_INTEGER) };
        indexDef = new MOTableIndex(subIndexes, false);
        colTypeCnt = 0;
    }

    /**
     * @param oid the root oid of this table
     */
    public MOTableBuilder(OID oid) {
        this.tableRootOid = oid;
        subIndexes = new MOTableSubIndex[] { new MOTableSubIndex(
                SMIConstants.SYNTAX_INTEGER) };
        indexDef = new MOTableIndex(subIndexes, false);
        colTypeCnt = 0;

    }

    /**
     * Adds all column types {@link MOColumn} to this table.
     * Important to understand that you must add all types here before
     * adding any row values
     *
     * @param syntax use {@link SMIConstants}
     * @param access
     * @return
     */
    public MOTableBuilder addColumnType(int syntax, MOAccess access) {
        colTypeCnt++;
        columns.add(new MOColumn(colTypeCnt, syntax, access));
        return this;
    }

    /**
     *
     * @param variable Variable to register
     * @param col the second to last sub id of the data
     * @param row the last sub id of the data
     * @return updated builder
     */
    public MOTableBuilder setRowValue(Variable variable, int col, int row) {

        while (tableRows.size() <= row) {
            tableRows.add(new Variable[columns.size()]);
        }
        tableRows.get(row-1)[col-1] = variable;
        return this;
    }

    /**
     *
     * Make a MOTable using the current MOTableBuilder
     *
     * @return target MOTable
     */
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
        Log.d("Column count", ""+model.getColumnCount()+"\n");
        ifTable.setVolatile(true);
        return ifTable;
    }

}
