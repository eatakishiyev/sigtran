/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * MSNetworkCapability ::= OCTET STRING (SIZE (1..8)) -- This parameter carries
 * the value part of the MS Network Capability IE defined in -- 3GPP TS 24.008
 * [35].
 *
 * @author eatakishiyev
 */
public class MSNetworkCapability extends OctetString {

    public MSNetworkCapability() {
        super();
    }

    public MSNetworkCapability(byte[] value) {
        super(value);
    }

    @Override
    public int getMinLength() {
        return 1;
    }

    @Override
    public int getMaxLength() {
        return 8;
    }
}
