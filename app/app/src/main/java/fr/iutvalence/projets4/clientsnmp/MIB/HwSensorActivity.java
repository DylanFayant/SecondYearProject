package fr.iutvalence.projets4.clientsnmp.MIB;

import android.os.StrictMode;

import org.snmp4j.smi.OID;

import java.io.FileInputStream;

/**
 * Class who gives us the device's Temperature
 * Created by Thundermist on 09/01/17.
 */
public class HwSensorActivity implements MIBComposite, MIBElement<Long> {

    /**
     *
     */
    private byte[] mBuffer = new byte[4096];

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
     * Get the device's temperature
     * @return A Long temparature un celsius degrees multiplied by 1000
     */
    @Override
    public Long getValue() {
        String file = readFile("/sys/devices/virtual/thermal/thermal_zone0/temp", '\n');
        if(file != null)
        {
            return Long.parseLong(file);
        }
        return Long.valueOf(0);
    }

    /**
     * Returns the string of the given file's content until the endChar
     * @param file
     * @param endChar
     * @return String
     */
    private String readFile(String file, char endChar) {
        StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        FileInputStream is = null;
        try
        {
            is = new FileInputStream(file);
            int len = is.read(mBuffer);
            is.close();

            if(len>0)
            {
                int i;
                for(i=0;i<len;i++)
                {
                    if(mBuffer[i] == endChar)
                    {
                        break;
                    }
                }
                return new String(mBuffer,0,i);
            }
        }
        catch(java.io.FileNotFoundException e){}
        catch(java.io.IOException e){}
        finally
        {
            if(is != null)
            {
                try {
                    is.close();
                }
                catch (java.io.IOException e){}
            }
            StrictMode.setThreadPolicy(savedPolicy);
        }
        return null;
    }
}
