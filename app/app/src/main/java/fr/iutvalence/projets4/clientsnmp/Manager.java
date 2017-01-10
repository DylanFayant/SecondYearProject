package fr.iutvalence.projets4.clientsnmp;

import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Created by simon on 09/01/2017.
 *  V3
 */

public class Manager{
    /**
     * Adresse internet sous forme protocole/ip:port
     */

    private String address;
    private Snmp snmp;
    private String ver3UserName = "michou";
    private String ver3AuthPasscode = "admin user phrase";

    public Manager(String address){
        this.address = address;
        try{
            start();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void start() throws IOException{
        TransportMapping transport = new DefaultUdpTransportMapping();

        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp = new Snmp(transport);
        snmp.getUSM().addUser(new OctetString(ver3UserName),
                new UsmUser(new OctetString(ver3UserName), AuthMD5.ID, new OctetString(ver3AuthPasscode), null, null));


        transport.listen();
    }
    private void stop() throws IOException{
        snmp.close();
    }

    public String getAsString(OID oid) throws IOException{
        ResponseEvent event = get(new OID[]{oid});
        return event.getResponse().get(0).getVariable().toString();
    }

    public String setIntFromString(int value, OID oid) throws IOException {

        ResponseEvent event = set(new OID[] { oid }, value);

        return event.getResponse().get(0).getVariable().toString();

    }

    public ResponseEvent get(OID oids[]) throws IOException{
        PDU pdu = new ScopedPDU();
        for(OID oid : oids){
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu,getTarget());
        if(event!=null) {
            PDU responsePDU = event.getResponse();
            if (responsePDU != null) {
                if (responsePDU.getErrorStatus() == PDU.noError) {
                    return event;
                }
            }
            throw new RuntimeException("response was null");
        }
        throw new RuntimeException("GET Timed OUT");
    }

    public ResponseEvent set(OID oids[], int value) throws IOException {

        PDU pdu;

        pdu = new ScopedPDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid, new Integer32(value)));
        }

        pdu.setType(PDU.SET);
        ResponseEvent event = null;

        try {
                event = snmp.send(pdu, getTarget(), null);

        } catch (IOException ioe) {
            System.out.println("Error SNMP SET");
        }

        if (event != null) {
            return event;
        }
        throw new RuntimeException("SET timed out");
    }

    private Target getTarget()
    {

        Address targetAddress = GenericAddress.parse(this.address);
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
        target.setSecurityName(new OctetString("MICHOU"));
        target.setVersion((SnmpConstants.version3));
        return target;

    }

}
