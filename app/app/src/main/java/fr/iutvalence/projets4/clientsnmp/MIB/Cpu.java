package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Thundermist on 09/01/17.
 */

public class Cpu implements MIBComposite, MIBElement<Double> {

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    @Override
    public Double getValue() {
        try
        {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat","r");
            String load = reader.readLine();
            reader.close();

            load = load.replace("cpu  ", "");

            String[] infos1 = load.split(" ");

            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e){}

            reader = new RandomAccessFile("/proc/stat","r");
            load = reader.readLine();
            reader.close();

            load = load.replace("cpu  ", "");

            String[] infos2 = load.split(" ");

            long user = Long.parseLong(infos2[0]) - Long.parseLong(infos1[0]);
            long nice = Long.parseLong(infos2[1]) - Long.parseLong(infos1[1]);
            long sys = Long.parseLong(infos2[2]) - Long.parseLong(infos1[2]);
            long idle = Long.parseLong(infos2[3]) - Long.parseLong(infos1[3]);

            long total = user + nice + sys + idle;

            long totalMoinsIdle = total - idle;

            double pourcentUtilisé = (double)totalMoinsIdle/(double) total;

            return  pourcentUtilisé;
        }
        catch (IOException ex )
        {
            ex.printStackTrace();
        }
        return 0.0;
    }
}
