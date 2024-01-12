/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * CellGlobalIdOrServiceAreaIdOrLAI ::= CHOICE {
 * cellGlobalIdOrServiceAreaIdFixedLength [0]
 * CellGlobalIdOrServiceAreaIdFixedLength, laiFixedLength [1] LAIFixedLength}
 *
 * @author eatakishiyev
 */
public class CellGlobalIdOrServiceAreaIdOrLAI implements MAPParameter {

    private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength;
    private LAIFixedLength lAIFixedLength;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CellGlobalIdOrServiceAreaIdOrLAI[")
                .append(cellGlobalIdOrServiceAreaIdFixedLength)
                .append(lAIFixedLength)
                .append("]");
        return sb.toString();
    }

    public CellGlobalIdOrServiceAreaIdOrLAI() {
    }

    public CellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength) {
        this.cellGlobalIdOrServiceAreaIdFixedLength = cellGlobalIdOrServiceAreaIdFixedLength;
    }

    public CellGlobalIdOrServiceAreaIdOrLAI(LAIFixedLength lAIFixedLength) {
        this.lAIFixedLength = lAIFixedLength;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);

        int lenPos = aos.StartContentDefiniteLength();
        if (cellGlobalIdOrServiceAreaIdFixedLength != null) {
            this.cellGlobalIdOrServiceAreaIdFixedLength.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else {
            this.lAIFixedLength.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        int length = ais.readLength();
        byte[] data = new byte[length];
        ais.read(data);

        this._decode(new AsnInputStream(data));
    }

    public void decode(AsnInputStream ais, int length) throws IOException, AsnException {
        byte[] data = new byte[length];
        ais.read(data);

        this._decode(new AsnInputStream(data));
    }

    private void _decode(AsnInputStream ais) throws AsnException, IOException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();

            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag Class[CONTEXT], found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLength();
                    this.cellGlobalIdOrServiceAreaIdFixedLength.decode(ais);
                    break;
                case 1:
                    this.lAIFixedLength = new LAIFixedLength();
                    this.lAIFixedLength.decode(ais);
                    break;
            }
        }
    }

    /**
     * @return the cellGlobalIdOrServiceAreaIdFixedLength
     */
    public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalIdOrServiceAreaIdFixedLength() {
        return cellGlobalIdOrServiceAreaIdFixedLength;
    }

    /**
     * @return the lAIFixedLength
     */
    public LAIFixedLength getlAIFixedLength() {
        return lAIFixedLength;
    }

}
