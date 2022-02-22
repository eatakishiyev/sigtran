/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

/**
 *
 * @author root
 */
public enum OperationClass {
    /*
     * 3GPP TS 29.002 V11.1.0 (2011-12)
     NOTE 1:  The class of an operation is not stated explicitly but is specified as well in the ASN.1 operation definition. 
     Class 1: RESULT and ERROR appear in ASN.1 operation definition. 
     Class 2: only ERROR appears in ASN.1 operation definition. 
     Class 3: only RESULT appears in ASN.1 operation definition. 
     Class 4: both RESULT and ERROR do not appear in ASN.1 operation definition.
     */

    /**
     * Class 1: RESULT and ERROR appear in ASN.1 operation definition.
     */
    CLASS1(1),
    /**
     * Class 2: only ERROR appears in ASN.1 operation definition. 
     */
    CLASS2(2),
    /**
     * Class 3: only RESULT appears in ASN.1 operation definition.
     */
    CLASS3(3),
    /**
     * Class 4: both RESULT and ERROR do not appear in ASN.1 operation definition.
     */
    CLASS4(4);
    private final int value;

    private OperationClass(int value) {
        this.value = value;
    }

    public static OperationClass getInstance(int value) {
        switch (value) {
            case 1:
                return CLASS1;
            case 2:
                return CLASS2;
            case 3:
                return CLASS3;
            case 4:
                return CLASS4;
            default:
                return CLASS1;
        }
    }

    public int value() {
        return this.value;
    }
}
