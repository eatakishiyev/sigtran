/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

/**
 *
 * @author eatakishiyev
 */
public class PointCode {

    private int mask;
    private int pointCode;

    public PointCode(int mask, int pointCode) {
        this.mask = mask;
        this.pointCode = pointCode;
    }

    /**
     * @return the mask
     */
    public int getMask() {
        return mask;
    }

    /**
     * @param mask the mask to set
     */
    public void setMask(int mask) {
        this.mask = mask;
    }

    /**
     * @return the pointCode
     */
    public int getPointCode() {
        return pointCode;
    }

    /**
     * @param pointCode the pointCode to set
     */
    public void setPointCode(int pointCode) {
        this.pointCode = pointCode;
    }

    @Override
    public String toString() {
        return String.format("PointCode[Mask = %s PointCode = %s]", mask, pointCode);
    }

}
