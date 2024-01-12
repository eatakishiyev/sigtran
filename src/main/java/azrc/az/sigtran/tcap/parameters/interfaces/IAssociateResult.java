/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters.interfaces;

import azrc.az.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface IAssociateResult extends Encodable {

    public static final int TAG_ = 0x02;
    public static final int TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean TAG_PC_PRIMITIVE = false;

    public ResultType getResultType();

    public void setResultType(ResultType rt);

    public enum ResultType {

        accepted(0),
        reject_permanent(1);
        private long type;

        private ResultType(long type) {
            this.type = type;
        }

        public static ResultType getResultType(long i) {
            if (i == 0) {
                return accepted;
            }
            if (i == 1) {
                return reject_permanent;
            }

            return null;
        }

        public long getType() {
            return this.type;
        }
    }
}
