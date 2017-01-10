package fr.iutvalence.projets4.clientsnmp.MIB;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import org.snmp4j.smi.OID;

/**
 * Created by ethis on 09/01/17.
 */

public class DiskUsage implements MIBComposite, MIBElement<Double> {

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
        StatFs internalStatFs = new StatFs( Environment.getRootDirectory().getAbsolutePath() );
        long internalTotal;
        long internalFree;

        StatFs externalStatFs = new StatFs( Environment.getExternalStorageDirectory().getAbsolutePath() );
        long externalTotal;
        long externalFree;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = ( internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong() );
            internalFree = ( internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong() );
            externalTotal = ( externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong() );
            externalFree = ( externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong() );
        }
        else {
            internalTotal = ( (long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize() );
            internalFree = ( (long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize() );
            externalTotal = ( (long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize() );
            externalFree = ( (long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize() );
        }

        long total = internalTotal + externalTotal;
        long free = internalFree + externalFree;
        long used = total - free;

        return (double)used;
    }
}
