package fr.iutvalence.projets4.clientsnmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Created by simon on 09/01/2017.
 * Plutôt V3
 */

public class Manager{
    /**
     * Adresse internet sous forme protocole/ip:port
     */
    private String address;
    private Snmp snmp;

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
        snmp = new Snmp(transport);
        transport.listen();
    }
    private void stop() throws IOException{
        snmp.close();
    }

    public String getAsString(OID oid) throws IOException{
        ResponseEvent event = get(new OID[]{oid});
        return event.getResponse().get(0).getVariable().toString();
    }

    public ResponseEvent get(OID oids[]) throws IOException{
        PDU pdu = new ScopedPDU();
        for(OID oid : oids){
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event=snmp.send(pdu,getTarget());
        if(event!=null){
            return event;
        }
        throw new RuntimeException("GET Timed OUT");
    }

    private Target getTarget(){

        Address targetAddress = GenericAddress.parse(this.address);
        CommunityTarget target = new CommunityTarget();
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString("MICHOU"));
        target.setVersion((SnmpConstants.version3));
        return target;

    }

}
