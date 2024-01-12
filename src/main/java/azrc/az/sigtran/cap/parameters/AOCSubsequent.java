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
 * AOCSubsequent ::= SEQUENCE { cAI-GSM0224 [0] CAI-GSM0224 ,
 * tariffSwitchInterval [1] INTEGER (1..86400) OPTIONAL } --
 * tariffSwitchInterval is measured in 1 second units
 *
 * @author eatakishiyev
 */
public class AOCSubsequent {

    private CAI_GSM0224 caiGSM0224;
    private Integer tariffSwitchInterval;
    public static final int MIN_TARIFF_SWITCH_INTERVAL = 1;
    public static final int MAX_TARIFF_SWITCH_INTERVAL = 86400;

    public AOCSubsequent() {
    }

    public AOCSubsequent(CAI_GSM0224 caiGSM0224) {
        this.caiGSM0224 = caiGSM0224;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IOException {

        if (this.caiGSM0224 == null) {
            throw new AsnException("Mandatory CAI-GSM0224 parameter expected.");
        }

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        this.caiGSM0224.encode(0, Tag.CLASS_CONTEXT_SPECIFIC, aos);

        if (tariffSwitchInterval != null) {
            this._doCheck();

            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, tariffSwitchInterval);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this._decode(tmpAis);
    }

    public void decode(AsnInputStream ais, int length) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStreamData(length);
        this._decode(tmpAis);
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();
            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag class 2, found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.caiGSM0224 = new CAI_GSM0224();
                    this.caiGSM0224.decode(ais);
                    break;
                case 1:
                    this.tariffSwitchInterval = ((Long) ais.readInteger()).intValue();
                    this._doCheck();
                    break;
            }
        }

        if (this.caiGSM0224 == null) {
            throw new AsnException("Mandatory CAI-GSM0224 parameter expected.");
        }
    }

    public CAI_GSM0224 getCaiGSM0224() {
        return this.caiGSM0224;
    }

    /**
     * @return the tariffSwitchInterval
     */
    public Integer getTariffSwitchInterval() {
        return tariffSwitchInterval;
    }

    /**
     * @param tariffSwitchInterval the tariffSwitchInterval to set
     */
    public void setTariffSwitchInterval(Integer tariffSwitchInterval) {
        this.tariffSwitchInterval = tariffSwitchInterval;
    }

    private void _doCheck() throws ParameterOutOfRangeException {
        if (tariffSwitchInterval < MIN_TARIFF_SWITCH_INTERVAL
                || tariffSwitchInterval > MAX_TARIFF_SWITCH_INTERVAL) {
            throw new ParameterOutOfRangeException("Parameter TariffSwitchInterval is out of range [1..86400]");
        }
    }

}
