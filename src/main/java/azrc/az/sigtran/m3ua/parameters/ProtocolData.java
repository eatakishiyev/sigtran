/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import azrc.az.sigtran.m3ua.MTPTransferMessage;
import azrc.az.sigtran.m3ua.NetworkIndicator;
import azrc.az.sigtran.m3ua.ServiceIdentificator;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import azrc.az.sigtran.utils.ByteUtils;

/**
 *
 * @author root
 */
public class ProtocolData implements Parameter {

    private final ParameterTag tag = ParameterTag.PROTOCOL_DATA;
    private int sctpStreamNumber = 0;
    /**
     * Protocol Data Length The Parameter Length field contains the size of the
     * parameter in bytes, including the Parameter Tag, Parameter Length, and
     * Parameter Value fields. Thus, a parameter whit a zero-length Parameter
     * Value field would have a Length field of 4. The Parameter Length does not
     * include any padding bytes
     */
    //public  short Length;
//  SIF  
    private byte[] userProtocolData;
//   Routing Label  
    private int opc;
    private int dpc;
    private int sls = 0;
//  SIO  
    private ServiceIdentificator si;
    private NetworkIndicator ni;
    private int mp = 0;

    public ProtocolData() {
    }

    public ProtocolData(int opc, int dpc, ServiceIdentificator si, NetworkIndicator ni, int mp, int sls, byte[] userProtocolData) {
        this.opc = opc;
        this.dpc = dpc;
        this.si = si;
        this.ni = ni;
        this.mp = mp;
        this.sls = sls;
        this.userProtocolData = userProtocolData;
    }

    public ProtocolData(MTPTransferMessage mtpTransferMessage) {
        this(mtpTransferMessage.getOpc(), mtpTransferMessage.getDpc(), mtpTransferMessage.getSi(), mtpTransferMessage.getNi(), mtpTransferMessage.getMp(), mtpTransferMessage.getSls(), mtpTransferMessage.getUserData());
    }

    @Override
    public String toString() {
        return String.format("ProtocolData [Opc = %s Dpc = %s Si = %s Ni = %s "
                + "Mp = %s Sls = %s Data = %s]", this.opc, this.dpc, this.si, this.ni, 
                this.mp, this.sls, ByteUtils.bytes2Hex(userProtocolData));
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        ByteArrayInputStream tmpBais = new ByteArrayInputStream(data);
        this.opc = 0;
        this.opc = this.opc | tmpBais.read() & 0xff;
        this.opc = this.opc << 8;
        this.opc = this.opc | tmpBais.read() & 0xff;
        this.opc = this.opc << 8;
        this.opc = this.opc | tmpBais.read() & 0xff;
        this.opc = this.opc << 8;
        this.opc = this.opc | tmpBais.read() & 0xff;

        this.dpc = 0;
        this.dpc = this.dpc | tmpBais.read() & 0xff;
        this.dpc = this.dpc << 8;
        this.dpc = this.dpc | tmpBais.read() & 0xff;
        this.dpc = this.dpc << 8;
        this.dpc = this.dpc | tmpBais.read() & 0xff;
        this.dpc = this.dpc << 8;
        this.dpc = this.dpc | tmpBais.read() & 0xff;

        this.si = ServiceIdentificator.getInstance(tmpBais.read());
        this.ni = NetworkIndicator.getInstance(tmpBais.read());
        this.mp = tmpBais.read();
        this.sls = tmpBais.read();

        this.userProtocolData = new byte[tmpBais.available()];
        tmpBais.read(this.userProtocolData);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {

        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream(272);

        tmpBaos.write((this.opc >> 24) & 0xFF);
        tmpBaos.write((this.opc >> 16) & 0xFF);
        tmpBaos.write((this.opc >> 8) & 0xFF);
        tmpBaos.write(this.opc & 0xFF);

        tmpBaos.write((this.dpc >> 24) & 0xFF);
        tmpBaos.write((this.dpc >> 16) & 0xFF);
        tmpBaos.write((this.dpc >> 8) & 0xFF);
        tmpBaos.write(this.dpc & 0xFF);

        tmpBaos.write(this.si.value() & 0xFF);
        tmpBaos.write(this.ni.value() & 0xFF);
        tmpBaos.write(this.mp & 0xFF);
        tmpBaos.write(this.sls & 0xFF);

        tmpBaos.write(this.userProtocolData);
        byte[] data = tmpBaos.toByteArray();
        baos.encode(tag, data);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the opc
     */
    public int getOpc() {
        return opc;
    }

    /**
     * @param opc the opc to set
     */
    public void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * @return the dpc
     */
    public int getDpc() {
        return dpc;
    }

    /**
     * @param dpc the dpc to set
     */
    public void setDpc(int dpc) {
        this.dpc = dpc;
    }

    /**
     * @return the si
     */
    public ServiceIdentificator getSi() {
        return si;
    }

    /**
     * @param si the si to set
     */
    public void setSi(ServiceIdentificator si) {
        this.si = si;
    }

    /**
     * @return the ni
     */
    public NetworkIndicator getNi() {
        return ni;
    }

    /**
     * @param ni the ni to set
     */
    public void setNi(NetworkIndicator ni) {
        this.ni = ni;
    }

    /**
     * @return the mp
     */
    public int getMp() {
        return mp;
    }

    /**
     * @param mp the mp to set
     */
    public void setMp(int mp) {
        this.mp = mp;
    }

    /**
     * @return the sls
     */
    public int getSls() {
        return sls;
    }

    /**
     * @param sls the sls to set
     */
    public void setSls(int sls) {
        this.sls = sls;
    }

    /**
     * @param userProtocolData the userProtocolData to set
     */
    public void setUserProtocolData(byte[] userProtocolData) {
        this.userProtocolData = userProtocolData;
    }

    public byte[] getUserProtocolData() {
        return this.userProtocolData;
    }

    /**
     * @return the sctpStreamNumber
     */
    public int getSctpStreamNumber() {
        return sctpStreamNumber;
    }

    /**
     * @param sctpStreamNumber the sctpStreamNumber to set
     */
    public void setSctpStreamNumber(int sctpStreamNumber) {
        this.sctpStreamNumber = sctpStreamNumber;
    }
}
