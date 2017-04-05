package fr.iutvalence.projets4.clientsnmp.MIB;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import org.snmp4j.smi.OID;

/**
 * Class who gives us the device's Disk Usage
 * PROBLEM : It gives only the internal disk usage (it don't work with SD cards memories)
 * Created by ethis on 09/01/17.
 */
public class HwDiskUsage implements MIBComposite, MIBElement<Double> {

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
     * Get the device's Disk Usage
     * Only the internal storage !
     * @return A double percentage between 0 and 1 of the Disk Usage
     */
    @Override
    public Double getValue() {
        return ((double)getInternalUsedSpace()/(double)getInternalStorageSpace())*100;
    }

    /**
     * Get the internal storage space of the device
     * @return long bytes size
     */
    public long getInternalStorageSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());

        long total = 0L;

        if(Build.VERSION.SDK_INT < 27) {
            total = ((long) statFs.getBlockCount() * (long) statFs.getBlockSize());
        }
        else
        {
            total = statFs.getAvailableBlocksLong() * statFs.getAvailableBlocksLong();
        }

        return total;
    }

    /**
     * Get the internal used space of the device
     * @return long bytes size
     */
    public long getInternalUsedSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());

        long total = 0L;
        long free  = 0L;

        if(Build.VERSION.SDK_INT < 27) {
            total = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize());
            free  = ((long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize());
        }
        else
        {
            total = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
            free  = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
        }

        long busy  = total - free;
        return busy;
    }

}
