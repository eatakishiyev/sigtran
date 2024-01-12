/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

/**
 *
 * @author eatakishiyev
 */
public class OriginationPointCode {

    private Integer mask;
    private Integer pointCode;

    public OriginationPointCode(Integer mask, Integer pointCode) {
        this.mask = mask;
        this.pointCode = pointCode;
    }

    /**
     * @return the mask
     */
    public Integer getMask() {
        return mask;
    }

    /**
     * @param mask the mask to set
     */
    public void setMask(Integer mask) {
        this.mask = mask;
    }

    /**
     * @return the pointCode
     */
    public Integer getPointCode() {
        return pointCode;
    }

    /**
     * @param pointCode the pointCode to set
     */
    public void setPointCode(Integer pointCode) {
        this.pointCode = pointCode;
    }
}
