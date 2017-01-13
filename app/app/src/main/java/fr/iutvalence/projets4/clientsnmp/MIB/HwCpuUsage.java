package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Class who gives us the device's CPU Usage
 * PROBLEM : It gives an approximate value
 * Created by Thundermist on 09/01/17.
 */
public class HwCpuUsage implements MIBComposite, MIBElement<Double> {

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
     * Get the device's CPU Usage
     * Approximate value !
     * @return A double percentage between 0 and 1 of the CPU Usage
     */
    @Override
    public Double getValue() {
        try
        {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            double value = (double)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

            return (double)Math.round(value*100.0)/100.0;
        }
        catch (IOException ex )
        {
            ex.printStackTrace();
        }
        return 0.0;
    }
}
