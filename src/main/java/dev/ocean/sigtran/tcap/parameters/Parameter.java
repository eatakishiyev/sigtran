/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface Parameter {

    public void encode(AsnOutputStream aos) throws Exception;

    public void decode(AsnInputStream ais) throws Exception;

    public void setData(byte[] data);

    public byte[] getData();
}
