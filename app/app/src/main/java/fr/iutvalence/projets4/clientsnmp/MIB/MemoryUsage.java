package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ethis on 09/01/17.
 */

public class MemoryUsage implements MIBComposite, MIBElement<Double> {

    @Override
    public MIBComposite getComposite(OID oid) {
        return this;
    }

    @Override
    public void setComposite(OID oid, MIBComposite mibComposite) {

    }

    private MemorySize getMemorySize() {
        final Pattern PATTERN = Pattern.compile("([a-zA-Z]+):\\s*(\\d+)");

        MemorySize result = new MemorySize();
        String line;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            while ((line = reader.readLine()) != null) {
                Matcher m = PATTERN.matcher(line);
                if (m.find()) {
                    String name = m.group(1);
                    String size = m.group(2);

                    if (name.equalsIgnoreCase("MemTotal")) {
                        result.total = Long.parseLong(size);
                    } else if (name.equalsIgnoreCase("MemFree") || name.equalsIgnoreCase("Buffers") ||
                            name.equalsIgnoreCase("Cached")) {
                        result.free += Long.parseLong(size);
                    }
                }
            }
            reader.close();

            result.total *= 1024;
            result.free *= 1024;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static class MemorySize {
        public long total = 0;
        public long free = 0;
    }

    @Override
    public Double getValue() {
        MemorySize memorySize = this.getMemorySize();

        long usedMemorySize = memorySize.total - memorySize.free;

        return ((double) usedMemorySize /(double) memorySize.total);
    }
}
