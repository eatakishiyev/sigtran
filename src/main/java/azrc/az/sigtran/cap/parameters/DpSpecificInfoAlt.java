/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtBasicServiceCode;
import azrc.az.isup.parameters.CalledPartyNumber;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class DpSpecificInfoAlt {

    private ServiceChangeSpecificInfo oServiceChangeSpecificInfo;
    private ServiceChangeSpecificInfo tServiceChangeSpecificInfo;
    private CollectedInfoSpecificInfo collectedInfoSpecificInfo;

    public DpSpecificInfoAlt() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (oServiceChangeSpecificInfo != null) {
            this.oServiceChangeSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        }

        if (tServiceChangeSpecificInfo != null) {
            this.tServiceChangeSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }

        if (collectedInfoSpecificInfo != null) {
            this.collectedInfoSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, IllegalNumberFormatException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();
            this.decode_(tmpAis);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode_(AsnInputStream ais) throws IncorrectSyntaxException, IllegalNumberFormatException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Received incorrect tag. Expection Tag[2], found Tag[%s]", ais.getTagClass()));
                }

                switch (tag) {
                    case 0:
                        this.oServiceChangeSpecificInfo.decode(ais);
                        break;
                    case 1:
                        this.tServiceChangeSpecificInfo.decode(ais);
                        break;
                    case 2:
                        this.collectedInfoSpecificInfo.decode(ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Received incorrect tag. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the oServiceChangeSpecificInfo
     */
    public ServiceChangeSpecificInfo getoServiceChangeSpecificInfo() {
        return oServiceChangeSpecificInfo;
    }

    /**
     * @param oServiceChangeSpecificInfo the oServiceChangeSpecificInfo to set
     */
    public void setoServiceChangeSpecificInfo(ServiceChangeSpecificInfo oServiceChangeSpecificInfo) {
        this.oServiceChangeSpecificInfo = oServiceChangeSpecificInfo;
    }

    /**
     * @return the tServiceChangeSpecificInfo
     */
    public ServiceChangeSpecificInfo gettServiceChangeSpecificInfo() {
        return tServiceChangeSpecificInfo;
    }

    /**
     * @param tServiceChangeSpecificInfo the tServiceChangeSpecificInfo to set
     */
    public void settServiceChangeSpecificInfo(ServiceChangeSpecificInfo tServiceChangeSpecificInfo) {
        this.tServiceChangeSpecificInfo = tServiceChangeSpecificInfo;
    }

    /**
     * @return the collectedInfoSpecificInf
     */
    public CollectedInfoSpecificInfo getCollectedInfoSpecificInf() {
        return collectedInfoSpecificInfo;
    }

    /**
     * @param collectedInfoSpecificInf the collectedInfoSpecificInf to set
     */
    public void setCollectedInfoSpecificInf(CollectedInfoSpecificInfo collectedInfoSpecificInf) {
        this.collectedInfoSpecificInfo = collectedInfoSpecificInf;
    }

    public class ServiceChangeSpecificInfo {

        private ExtBasicServiceCode extBasicServiceCode;
        private InitiatorOfServiceChange initiatorOfServiceChange;
        private NatureOfServiceChange natureOfServiceChange;

        public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
            try {
                aos.writeTag(tagClass, false, tag);
                int lenPos = aos.StartContentDefiniteLength();

                if (extBasicServiceCode != null) {
                    extBasicServiceCode.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
                }

                if (initiatorOfServiceChange != null) {
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, initiatorOfServiceChange.value());
                }

                if (natureOfServiceChange != null) {
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, natureOfServiceChange.value());
                }

                aos.FinalizeContent(lenPos);
            } catch (AsnException | IOException ex) {
                throw new IncorrectSyntaxException(ex);
            }
        }

        public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
            try {
                AsnInputStream tmpAis = ais.readSequenceStream();
                this.decode_(tmpAis);
            } catch (AsnException | IOException ex) {
                throw new IncorrectSyntaxException(ex);
            }
        }

        private void decode_(AsnInputStream ais) throws IncorrectSyntaxException {
            try {
                while (ais.available() > 0) {
                    int tag = ais.readTag();
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new AsnException(String.format("Received invalid tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
                    }

                    switch (tag) {
                        case 0:
                            this.extBasicServiceCode = new ExtBasicServiceCode();
                            this.extBasicServiceCode.decode(ais);
                            break;
                        case 1:
                            this.initiatorOfServiceChange = InitiatorOfServiceChange.getInstance((int) ais.readInteger());
                            break;
                        case 2:
                            this.natureOfServiceChange = NatureOfServiceChange.getInstance((int) ais.readInteger());
                            break;
                    }
                }
            } catch (AsnException | IOException ex) {
                throw new IncorrectSyntaxException(ex);
            }
        }

        /**
         * @return the extBasicServiceCode
         */
        public ExtBasicServiceCode getExtBasicServiceCode() {
            return extBasicServiceCode;
        }

        /**
         * @param extBasicServiceCode the extBasicServiceCode to set
         */
        public void setExtBasicServiceCode(ExtBasicServiceCode extBasicServiceCode) {
            this.extBasicServiceCode = extBasicServiceCode;
        }

        /**
         * @return the initiatorOfServiceChange
         */
        public InitiatorOfServiceChange getInitiatorOfServiceChange() {
            return initiatorOfServiceChange;
        }

        /**
         * @param initiatorOfServiceChange the initiatorOfServiceChange to set
         */
        public void setInitiatorOfServiceChange(InitiatorOfServiceChange initiatorOfServiceChange) {
            this.initiatorOfServiceChange = initiatorOfServiceChange;
        }

        /**
         * @return the natureOfServiceChange
         */
        public NatureOfServiceChange getNatureOfServiceChange() {
            return natureOfServiceChange;
        }

        /**
         * @param natureOfServiceChange the natureOfServiceChange to set
         */
        public void setNatureOfServiceChange(NatureOfServiceChange natureOfServiceChange) {
            this.natureOfServiceChange = natureOfServiceChange;
        }

    }

    public class CollectedInfoSpecificInfo {

        private CalledPartyNumber calledPartyNumber;

        public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
            try {
                aos.writeTag(tagClass, false, tag);
                int lenPos = aos.StartContentDefiniteLength();

                if (calledPartyNumber != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                    baos.write(calledPartyNumber.encode());
                    aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 0);
                    int lenPos_ = aos.StartContentDefiniteLength();
                    aos.write(baos.toByteArray());
                    aos.FinalizeContent(lenPos_);
                }

                aos.FinalizeContent(lenPos);
            } catch (AsnException | IllegalNumberFormatException | IOException ex) {
                throw new IncorrectSyntaxException(ex.getMessage());
            }
        }

        public void decode(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException {
            AsnInputStream tmpAis = ais.readSequenceStream();
            this.decode_(tmpAis);
        }

        private void decode_(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag != 0 || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new AsnException(String.format("Received incorrect tag. Expecting Tag[0] TagClass[2], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                }

                byte[] data = new byte[ais.readLength()];
                ais.read(data);

                this.calledPartyNumber = new CalledPartyNumber();
                this.calledPartyNumber.decode((data));
            }
        }

        /**
         * @return the calledPartyNumber
         */
        public CalledPartyNumber getCalledPartyNumber() {
            return calledPartyNumber;
        }

        /**
         * @param calledPartyNumber the calledPartyNumber to set
         */
        public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber) {
            this.calledPartyNumber = calledPartyNumber;
        }

    }
}
