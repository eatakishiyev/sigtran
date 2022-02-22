/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SMS-CAMEL-TDP-DataList::= SEQUENCE SIZE (1..maxNumOfCamelTDPData) OF
 * SMS-CAMEL-TDP-Data
 * -- SMS-CAMEL-TDP-DataList shall not contain more than one instance of
 * -- SMS-CAMEL-TDP-Data containing the same value for
 * sms-TriggerDetectionPoint.
 * @author eatakishiyev
 */
public class SMSCamelTDPDataList {

    private final List<SMSCamelTDPData> smsCamelTDPDatas;

    public SMSCamelTDPDataList() {
        this.smsCamelTDPDatas = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (smsCamelTDPDatas.size() < 1
                || smsCamelTDPDatas.size() > Constants.maxNumOfCamelTDPData) {
            throw new UnexpectedDataException(String.format("SMSCamelTDPData count must be in [1..10] range. Current count is " + smsCamelTDPDatas.size()));
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (SMSCamelTDPData sMSCamelTDPData : smsCamelTDPDatas) {
                sMSCamelTDPData.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    SMSCamelTDPData sMSCamelTDPData = new SMSCamelTDPData();
                    sMSCamelTDPData.decode(ais.readSequenceStream());
                    smsCamelTDPDatas.add(sMSCamelTDPData);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (smsCamelTDPDatas.size() < 1
                    || smsCamelTDPDatas.size() > Constants.maxNumOfCamelTDPData) {
                throw new UnexpectedDataException(String.format("SMSCamelTDPData count must be in [1..10] range. Current count is " + smsCamelTDPDatas.size()));
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<SMSCamelTDPData> getSmsCamelTDPDatas() {
        return smsCamelTDPDatas;
    }

}
