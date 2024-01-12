/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

import java.util.Arrays;

/**
 *
 * @author root
 */
public class AffectedPointCode implements Parameter {

    private final ParameterTag tag = ParameterTag.AFFECTED_POINT_CODE;
    private PointCode[] pointCodes;

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        ByteArrayInputStream tmpBais = new ByteArrayInputStream(data);

        ArrayList<PointCode> pointCodeList = new ArrayList<>();
        while (tmpBais.available() > 0) {
            int mask = tmpBais.read() & 0xFF;
            int iPointCode = tmpBais.read() & 0xFF;
            iPointCode = iPointCode << 8;
            iPointCode = iPointCode | tmpBais.read() & 0xFF;
            iPointCode = iPointCode << 8;
            iPointCode = iPointCode | tmpBais.read() & 0xFF;
            PointCode pointCode = new PointCode(mask, iPointCode);
            pointCodeList.add(pointCode);
        }

        pointCodes = new PointCode[pointCodeList.size()];
        pointCodeList.toArray(pointCodes);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
        for (PointCode pointCode : this.pointCodes) {
            tmpBaos.write(pointCode.getMask());
            tmpBaos.write((pointCode.getPointCode() >> 16) & 0xFF);
            tmpBaos.write((pointCode.getPointCode() >> 8) & 0xFF);
            tmpBaos.write((pointCode.getPointCode()) & 0xFF);
        }
        baos.encode(this.tag, tmpBaos.toByteArray());
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the oAffectedPointCode
     */
    public PointCode[] getAffectedPointCodes() {
        return pointCodes;
    }

    /**
     * @param pointCodes
     */
    public void setoAffectedPointCodes(PointCode[] pointCodes) {
        this.pointCodes = pointCodes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AffectedPointCodes[");
        sb.append(Arrays.toString(pointCodes));
        sb.append("]");
        return sb.toString();
    }
}
