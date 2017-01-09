package fr.iutvalence.projets4.clientsnmp;

import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

import java.io.File;
import java.io.IOException;

/**
 * Created by simon on 09/01/2017.
 */

public class Agent extends BaseAgent{

    private static UsmUser Michou = new UsmUser(new OctetString("authPrivMd5Aes"),
            AuthMD5.ID,
            new OctetString("MICHOU"),
            PrivAES128.ID,
            new OctetString("MICHOU"));

    static{
        LogFactory.setLogFactory(new Log4jLogFactory());
    }

    private String address;

    public Agent(String address) throws IOException {
        super(new File("conf.agent"), new File("bootCounter.agent"), new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
        this.address=address;
    }

    @Override
    protected void registerManagedObjects() {

    }

    protected void registerManagedObjects(ManagedObject mo) {
        try{
            server.register(mo, null);
        }catch(DuplicateRegistrationException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unregisterManagedObjects() {

    }

    protected void unregisterManagedObjects(MOGroup moGroup) {
        moGroup.unregisterMOs(server, getContext(moGroup));

    }


    @Override
    protected void addUsmUser(USM usm) {
        usm.addUser(new OctetString("MICHOU"),Michou);

//yoyoyoyoyoyo v3
    }

    @Override
    protected void addNotificationTargets(SnmpTargetMIB snmpTargetMIB, SnmpNotificationMIB snmpNotificationMIB) {

    }

    @Override
    protected void addViews(VacmMIB vacm) {
        vacm.addGroup(SecurityModel.SECURITY_MODEL_USM, new OctetString("MD5User"),new OctetString("MICHOU"), StorageType.nonVolatile);//TO CHECK
        vacm.addAccess(new OctetString("MD5User"), new OctetString("MICHOU"), SecurityModel.SECURITY_MODEL_USM, SecurityLevel.AUTH_NOPRIV, MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"), new OctetString("fullWriteView"),new OctetString("fullNotifyView"),StorageType.nonVolatile);
        vacm.addViewTreeFamily( new OctetString("fullReadView"), new OID(""), new OctetString(), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
    }

    @Override
    protected void addCommunities(SnmpCommunityMIB snmpCommunityMIB) {

    }
}
