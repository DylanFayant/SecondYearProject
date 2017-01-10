package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Thundermist on 09/01/17.
 */

public class Cpu implements MIBComposite, MIBElement<Float> {

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Float getValue() {
        try
        {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat","r");
            String load = reader.readLine();

            String[] toks = load.split(" +");

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2])+Long.parseLong(toks[3])+Long.parseLong(toks[5])+Long.parseLong(toks[6])+Long.parseLong(toks[7])+Long.parseLong(toks[8]);
            try
            {
                Thread.sleep(300);
            }
            catch(Exception e){}
            reader.seek(0);
            load  = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2])+Long.parseLong(toks[3])+Long.parseLong(toks[5])+Long.parseLong(toks[6])+Long.parseLong(toks[7])+Long.parseLong(toks[8]);

            return (float) ((cpu2 - idle2) / ((cpu2 + idle2) - (cpu1 + idle1)));
        }
        catch (IOException ex )
        {
            ex.printStackTrace();
        }
        return 0.0f;
    }
}
