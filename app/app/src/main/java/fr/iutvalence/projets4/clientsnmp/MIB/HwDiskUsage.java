package fr.iutvalence.projets4.clientsnmp.MIB;

import android.os.Environment;
import android.os.StatFs;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public class HwDiskUsage implements MIBComposite, MIBElement<Double> {

    private static final long KILOBYTE = 1024;

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    /**
     * This class doesn't work properly !
     * @return
     */
    @Override
    public Double getValue() {
        return (double)getInternalUsedSpace()/(double)getInternalStorageSpace();
    }

    public long getInternalStorageSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long total = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize());
        return total;
    }

    public long getInternalFreeSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long free  = ((long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize());
        return free;
    }

    public long getInternalUsedSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long total = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize());
        long free  = ((long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize());
        long busy  = total - free;
        return busy;
    }

}
