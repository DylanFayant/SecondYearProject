package fr.iutvalence.projets4.clientsnmp.MIB;

import org.snmp4j.smi.OID;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class who gives us the device's Memory Usage
 * Created by ethis on 09/01/17.
 */
public class HwMemoryUsage implements MIBComposite, MIBElement<Double> {

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
     * Get the memory size in bytes
     * @return MemorySize object who contains total memory and free memory in bytes
     */
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
                    } else if (name.equalsIgnoreCase("MemFree") ||
                            name.equalsIgnoreCase("Cached")) {
                        result.free += Long.parseLong(size);
                    }
                }
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Creates "type" MemorySize
     */
    private static class MemorySize {
        /**
         * Total memory size in bytes
         */
        public long total = 0;
        /**
         * Free memory size in bytes
         */
        public long free = 0;
    }

    /**
     * Get the device's Memory Usage
     * @return A double percentage between 0 and 1 of the Memory Usage
     */
    @Override
    public Double getValue() {
        MemorySize memorySize = this.getMemorySize();

        long usedMemorySize = memorySize.total - memorySize.free;

        return (double) usedMemorySize / (double) memorySize.total;
    }
}
