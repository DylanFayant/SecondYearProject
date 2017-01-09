package fr.iutvalence.projets4.clientsnmp.MIB;

import android.support.annotation.NonNull;

import org.snmp4j.smi.OID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ethis on 09/01/17.
 */

public class MIBComponent implements MIBComposite {

    Map<Integer, MIBComposite> mibComposites;

    public MIBComponent() {
        this.mibComposites = new HashMap<Integer, MIBComposite>();
    }

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

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {
        int[] oidIntArray = oid.toIntArray();

        if(this.oidIsTheLast(oidIntArray))
        {
            this.mibComposites.put(oidIntArray[0], mibComposite);
        }
        else
        {
            OID oidWithoutFirstId = this.getOidWithoutFirstId(oidIntArray);

            MIBComposite sonMibComposite = this.mibComposites.get(oidIntArray[0]);

            if(sonMibComposite == null)
            {
                addComponent(oidIntArray[0]);
                sonMibComposite = this.mibComposites.get(oidIntArray[0]);
            }

            sonMibComposite.setComposite(oidWithoutFirstId, mibComposite);
        }
    }

    private void addComponent(int componentId) {
        this.mibComposites.put(componentId, new MIBComponent());
    }

    @NonNull
    private OID getOidWithoutFirstId(int[] oidIntArray) {
        return new OID(Arrays.copyOfRange(oidIntArray, 1, oidIntArray.length));
    }

    private boolean oidIsTheLast(int[] oidIntArray) {
        return oidIntArray.length == 1;
    }
}
