/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class ChangeOfPositionControlInfo {

    private List<ChangeOfLocation> changeOfLocations;

    public ChangeOfPositionControlInfo() {
    }

    public ChangeOfPositionControlInfo(List<ChangeOfLocation> changeOfLocations) {
        this.changeOfLocations = changeOfLocations;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, IOException {
        this.doCheck();

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        for (ChangeOfLocation changeOfLocation : changeOfLocations) {
            changeOfLocation.encode(aos);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        changeOfLocations = new ArrayList();

        while (tmpAis.available() > 0) {
            int tag = tmpAis.readTag();

            ChangeOfLocation changeOfLocation = ChangeOfLocation.create(ChangeOfLocation.Type.getIntance(tag));
            changeOfLocation.decode(tmpAis);

            changeOfLocations.add(changeOfLocation);
        }

        this.doCheck();
    }

    /**
     * @return the changeOfLocations
     */
    public List<ChangeOfLocation> getChangeOfLocations() {
        return changeOfLocations;
    }

    private void doCheck() throws AsnException {
        if (changeOfLocations == null || changeOfLocations.size() < 1 || changeOfLocations.size() > CAPSpecificBoundSet.NUM_OF_CHANGE_OF_POSITION_CONTROL_INFO) {
            throw new AsnException("Incorrect length of parameter.");
        }
    }
}
