/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class ODBData {

    private ODBGeneralData odbGeneralData;
    private ODBHPLMNData odbHPLMNData;
    private byte[] extensionContainer;

    public void encode(AsnOutputStream aos) {
    }

    /**
     * @return the odbGeneralData
     */
    public ODBGeneralData getOdbGeneralData() {
        return odbGeneralData;
    }

    /**
     * @param odbGeneralData the odbGeneralData to set
     */
    public void setOdbGeneralData(ODBGeneralData odbGeneralData) {
        this.odbGeneralData = odbGeneralData;
    }

    /**
     * @return the odbHPLMNData
     */
    public ODBHPLMNData getOdbHPLMNData() {
        return odbHPLMNData;
    }

    /**
     * @param odbHPLMNData the odbHPLMNData to set
     */
    public void setOdbHPLMNData(ODBHPLMNData odbHPLMNData) {
        this.odbHPLMNData = odbHPLMNData;
    }

    /**
     * @return the extensionContainer
     */
    public byte[] getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(byte[] extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
}
