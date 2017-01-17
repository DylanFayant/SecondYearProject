package fr.iutvalence.projets4.clientsnmp.AgentTest;

import org.snmp4j.agent.MOScope;
import org.snmp4j.agent.ManagedObjectValueAccess;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import fr.iutvalence.projets4.clientsnmp.MIB.MIBElement;

/**
 * This class creates and returns ManagedObjects
 *
 */
public class MOCreator {
    public static MOScalar createReadOnly(OID oid,Object value ){
        return new MOScalar(oid,
                MOAccessImpl.ACCESS_READ_ONLY,
                getVariable(value));
    }

    private static Variable getVariable(Object value) {
        if(value instanceof String) {
            return new OctetString((String)value);
        }
        throw new IllegalArgumentException("Unmanaged Type: " + value.getClass());
    }



}