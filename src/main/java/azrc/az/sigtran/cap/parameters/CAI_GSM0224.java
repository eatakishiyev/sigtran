/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class CAI_GSM0224 {

    private Integer e1;
    private Integer e2;
    private Integer e3;
    private Integer e4;
    private Integer e5;
    private Integer e6;
    private Integer e7;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 8191;

    public CAI_GSM0224() {
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws IOException, AsnException, ParameterOutOfRangeException {
        this._doCheck();

        aos.writeTag(tagClass, false, tag);
        int position = aos.StartContentDefiniteLength();

        if (e1 != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, e1);
        }

        if (e2 != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, e2);
        }

        if (e3 != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, e3);
        }

        if (e4 != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, e4);
        }

        if (e5 != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 4, e5);
        }

        if (e6 != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 5, e6);
        }

        if (e7 != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 6, e7);
        }

        aos.FinalizeContent(position);

    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();

        this._decode(tmpAis);
        this._doCheck();

    }

    public void decode(AsnInputStream ais, int length) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStreamData(length);

        this._decode(tmpAis);
        this._doCheck();
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();

            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag class 2, found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.e1 = ((Long) ais.readInteger()).intValue();
                    break;
                case 1:
                    this.e2 = ((Long) ais.readInteger()).intValue();
                    break;
                case 2:
                    this.e3 = ((Long) ais.readInteger()).intValue();
                    break;
                case 3:
                    this.e4 = ((Long) ais.readInteger()).intValue();
                    break;
                case 4:
                    this.e5 = ((Long) ais.readInteger()).intValue();
                    break;
                case 5:
                    this.e6 = ((Long) ais.readInteger()).intValue();
                    break;
                case 6:
                    this.e7 = ((Long) ais.readInteger()).intValue();
                    break;
            }
        }
    }

    public void _doCheck() throws ParameterOutOfRangeException {
        if ((e1 != null && (e1 < MIN_VALUE || e1 > MAX_VALUE))
                || (e2 != null && (e2 < MIN_VALUE || e2 > MAX_VALUE))
                || (e3 != null && (e3 < MIN_VALUE || e3 > MAX_VALUE))
                || (e4 != null && (e4 < MIN_VALUE || e4 > MAX_VALUE))
                || (e5 != null && (e5 < MIN_VALUE || e5 > MAX_VALUE))
                || (e6 != null && (e6 < MIN_VALUE || e6 > MAX_VALUE))
                || (e7 != null && (e7 < MIN_VALUE || e7 > MAX_VALUE))) {
            throw new ParameterOutOfRangeException("One of parameter out of range [0..8191]");
        }
    }

    public void setE1(Integer e1) {
        this.e1 = e1;
    }

    public Integer getE1() {
        return e1;
    }

    public void setE2(Integer e2) {
        this.e2 = e2;
    }

    public Integer getE2() {
        return e2;
    }

    public void setE3(Integer e3) {
        this.e3 = e3;
    }

    public Integer getE3() {
        return e3;
    }

    public void setE4(Integer e4) {
        this.e4 = e4;
    }

    public Integer getE4() {
        return e4;
    }

    public void setE5(Integer e5) {
        this.e5 = e5;
    }

    public Integer getE5() {
        return e5;
    }

    public void setE6(Integer e6) {
        this.e6 = e6;
    }

    public Integer getE6() {
        return e6;
    }

    public void setE7(Integer e7) {
        this.e7 = e7;
    }

    public Integer getE7() {
        return e7;
    }

}
