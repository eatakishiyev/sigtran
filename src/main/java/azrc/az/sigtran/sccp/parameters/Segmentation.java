/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import azrc.az.sigtran.sccp.messages.ProtocolClassEnum;
import azrc.az.sigtran.sccp.general.Encodable;

/**
 *
 * @author root
 */
public class Segmentation implements Encodable {

    public static final int PARAMETER_NAME = 0x10;
    private int firstSegmentIndication;
    private ProtocolClassEnum classSelected;
    private int remainingSegment;
    private int segmentationLocalReference;

    public Segmentation() {
    }

    public Segmentation(int firstSegmentIndication, ProtocolClassEnum classSelected, short remainingSegment, int segmentationLocalReference) throws IOException {
        this.firstSegmentIndication = firstSegmentIndication;
        this.classSelected = classSelected;
        this.remainingSegment = remainingSegment;
        this.segmentationLocalReference = segmentationLocalReference;
        if (this.remainingSegment < 0 || this.remainingSegment > 0xFF) {
            throw new IOException(String.format("Remainig Segment should be in range 1..15. Current is %d", this.remainingSegment));
        }

    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws IOException {

        baos.write(PARAMETER_NAME);
        baos.write(4);//length of parameter

        this.firstSegmentIndication = (this.firstSegmentIndication << 1) | classSelected.value();
        this.firstSegmentIndication = firstSegmentIndication << 2;//spare
        this.firstSegmentIndication = (firstSegmentIndication << 4) | remainingSegment;
        baos.write(this.firstSegmentIndication);

        byte[] slr = new byte[3];
        slr[0] = (byte) (this.segmentationLocalReference >> 16);
        slr[1] = (byte) (this.segmentationLocalReference >> 8);
        slr[2] = (byte) (this.segmentationLocalReference);

        baos.write(slr);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws IOException {

        int length = bais.read();//read length byte

        if (length > 4) {
            throw new IOException(String.format("Incorrect Segmentation parameter length."
                    + " Expected 4 octets, received %s octets.", length));
        }

        int firstOctet = bais.read() & 0xff;
        this.firstSegmentIndication = (byte) ((firstOctet >> 7) & 0x01);
        this.classSelected = ProtocolClassEnum.getInstance(((firstOctet >> 6) & 0x01));
        this.remainingSegment = (byte) (firstOctet & 0x0b1111);
        if (this.remainingSegment < 0 || this.remainingSegment > 0xFF) {
            throw new IOException(String.format("Remainig Segment should be in range 1..15. Current is %d", this.remainingSegment));
        }
        segmentationLocalReference = bais.read() & 0xff;
        segmentationLocalReference = (segmentationLocalReference << 8) | (bais.read() & 0xff);
        segmentationLocalReference = (segmentationLocalReference << 8) | (bais.read() & 0xff);
    }

    @Override
    public String toString() {
        return String.format("Segmentation [FSI = %s; "
                + "ClassSelected = %s; RS = %s; SLR = %s]", firstSegmentIndication,
                classSelected, remainingSegment, segmentationLocalReference);
    }

    /**
     * @return the FirstSegmentIndication
     */
    public int getFirstSegmentIndication() {
        return firstSegmentIndication;
    }

    /**
     * @return the ClassSelected
     */
    public ProtocolClassEnum getClassSelected() {
        return classSelected;
    }

    /**
     * @return the remainingSegment
     */
    public int getRemainingSegment() {
        return remainingSegment;
    }

    /**
     * @return the segmentationLocalReference
     */
    public int getSegmentationLocalReference() {
        return segmentationLocalReference;
    }
}
