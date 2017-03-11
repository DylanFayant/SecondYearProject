package fr.iutvalence.projets4.clientsnmp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.smi.OID;

import fr.iutvalence.projets4.clientsnmp.MO.MOTableBuilder;

import static junit.framework.Assert.assertEquals;

/**
 * Created by simon on 11/03/2017.
 */
@RunWith(AndroidJUnit4.class)
public class TableBuilderTest {
    @Test
    public void buildEmptyTable() throws Exception {
        OID baseOID=new OID("2.2.2.1");
        MOTableBuilder tab = new MOTableBuilder(baseOID);
        MOTable table = tab.build();
        assertEquals(0, table.getColumnCount());
        assertEquals(baseOID, table.getOID());
        assertEquals(null, table.getValue(baseOID));
        }

    @Test
    public void buildOneRealTable() throws Exception {
        OID baseOID=new OID("2.2.2.1");
        OID sysdescrOID=new OID("2.2.2.1.1.1");
        OID sysuptimeOID=new OID("2.2.2.1.1.2");
        OID nameOID=new OID("2.2.2.1.2.1");
        String name = "Phone";
        String sysdescr = "ARM based terminal";
        int sysuptime = 150;

        MOTableBuilder tab = new MOTableBuilder(baseOID);
        MOTable table = tab.build();


        assertEquals(0, table.getColumnCount());
        assertEquals(baseOID, table.getOID());
        assertEquals(null, table.getValue(baseOID));
    }


}
