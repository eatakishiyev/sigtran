/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * MS-Classmark2 ::= OCTET STRING (SIZE (3)) -- This parameter carries the value
 * part of the MS Classmark 2 IE defined in -- 3GPP TS 24.008 [35].
 *
 * @author eatakishiyev
 */
public class MSClassmark2 extends OctetString {

    public MSClassmark2() {
        super();
    }

    public MSClassmark2(byte[] value) {
        super(value);
    }

    @Override
    public int getMaxLength() {
        return 3;
    }

    @Override
    public int getMinLength() {
        return 3;
    }
}
