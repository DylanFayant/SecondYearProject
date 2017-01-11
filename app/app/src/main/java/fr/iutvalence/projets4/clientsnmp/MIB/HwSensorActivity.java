package fr.iutvalence.projets4.clientsnmp.MIB;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;

import com.google.android.gms.common.api.GoogleApiClient;

import org.snmp4j.smi.OID;

import java.io.FileInputStream;


/**
 * Created by Thundermist on 09/01/17.
 */

public class HwSensorActivity implements MIBComposite, MIBElement<Long> {


    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Long getValue() {
        String file = readFile("/sys/devices/virtual/thermal/thermal_zone0/temp", '\n');
        if(file != null)
        {
            return Long.parseLong(file);
        }
        return Long.valueOf(0);
    }

    private byte[] mBuffer = new byte[4096];
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
