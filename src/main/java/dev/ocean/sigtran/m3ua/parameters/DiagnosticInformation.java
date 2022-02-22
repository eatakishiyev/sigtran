/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import dev.ocean.sigtran.utils.ByteUtils;

/**
 *
 * @author root
 */
public class DiagnosticInformation implements Parameter {

    private final ParameterTag tag = ParameterTag.DIAGNOSTIC_INFORMATION;
    private byte[] diagnosticInformation;

    public DiagnosticInformation() {
    }

    public DiagnosticInformation(byte[] diagnosticInformation) {
        this.diagnosticInformation = diagnosticInformation;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        this.diagnosticInformation = new byte[length];
        bais.readData(diagnosticInformation);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        baos.encode(tag, this.diagnosticInformation);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the diagnosticInformation
     */
    public byte[] getDiagnosticInformation() {
        return diagnosticInformation;
    }

    /**
     * @param diagnosticInformation the diagnosticInformation to set
     */
    public void setDiagnosticInformation(byte[] diagnosticInformation) {
        this.diagnosticInformation = diagnosticInformation;
    }

    @Override
    public String toString() {
        return String.format("DiagnosticInformation [%s]", ByteUtils.bytes2Hex(diagnosticInformation));
    }

}
