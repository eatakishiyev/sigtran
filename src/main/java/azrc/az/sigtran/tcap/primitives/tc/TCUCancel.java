/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

/**
 *
 * @author root
 */
public class TCUCancel {

    private Long dialoguId;
    private Short invokeId;

    public Long getDialogueId() {
        return dialoguId;
    }

    /**
     * @param dialoguId the dialoguId to set
     */
    public void setDialogue(Long dialoguId) {
        this.dialoguId = dialoguId;
    }

    /**
     * @return the invokeId
     */
    public Short getInvokeId() {
        return invokeId;
    }

    /**
     * @param invokeId the invokeId to set
     */
    public void setInvokeId(Short invokeId) {
        this.invokeId = invokeId;
    }

}
