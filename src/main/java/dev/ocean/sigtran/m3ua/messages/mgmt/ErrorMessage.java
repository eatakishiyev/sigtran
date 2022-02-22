/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.messages.mgmt;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.Message;
import dev.ocean.sigtran.m3ua.MessageClass;
import dev.ocean.sigtran.m3ua.MessageType;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.parameters.AffectedPointCode;
import dev.ocean.sigtran.m3ua.parameters.DiagnosticInformation;
import dev.ocean.sigtran.m3ua.parameters.ErrorCode;
import dev.ocean.sigtran.m3ua.parameters.NetworkAppearance;
import dev.ocean.sigtran.m3ua.parameters.ParameterTag;
import dev.ocean.sigtran.m3ua.parameters.RoutingContext;

/**
 *
 * @author root
 */
public class ErrorMessage extends Message {

    private final MessageClass messageClass = MessageClass.MGMT;
    private final MessageType messageType = MessageType.ERR;
    private ErrorCode errorCode;
    private RoutingContext routingContext;
    private AffectedPointCode affectedPointCode;
    private NetworkAppearance networkAppearance;
    private DiagnosticInformation diagnosticInformation;

    /**
     * @return the errorCode
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorCode(ErrorCodes errorCode) {
        this.errorCode = new ErrorCode(errorCode);
    }

    /**
     * @return the routingContext
     */
    public RoutingContext getRoutingContext() {
        return routingContext;
    }

    /**
     * @param routingContext the routingContext to set
     */
    public void setRoutingContext(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    /**
     * @return the affectedPointCode
     */
    public AffectedPointCode getAffectedPointCode() {
        return affectedPointCode;
    }

    /**
     * @param affectedPointCode the affectedPointCode to set
     */
    public void setAffectedPointCode(AffectedPointCode affectedPointCode) {
        this.affectedPointCode = affectedPointCode;
    }

    /**
     * @return the networkAppearance
     */
    public NetworkAppearance getNetworkAppearance() {
        return networkAppearance;
    }

    /**
     * @param networkAppearance the networkAppearance to set
     */
    public void setNetworkAppearance(NetworkAppearance networkAppearance) {
        this.networkAppearance = networkAppearance;
    }

    /**
     * @return the diagnosticInformation
     */
    public DiagnosticInformation getDiagnosticInformation() {
        return diagnosticInformation;
    }

    /**
     * @param diagnosticInformation the diagnosticInformation to set
     */
    public void setDiagnosticInformation(DiagnosticInformation diagnosticInformation) {
        this.diagnosticInformation = diagnosticInformation;
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpBaos = new M3UAParameterByteArrayOutputStream();
        errorCode.encode(tmpBaos);
        if (routingContext != null) {
            routingContext.encode(tmpBaos);
        }
        if (networkAppearance != null) {
            networkAppearance.encode(tmpBaos);
        }
        if (affectedPointCode != null) {
            affectedPointCode.encode(tmpBaos);
        }
        if (diagnosticInformation != null) {
            diagnosticInformation.encode(tmpBaos);
        }
        byte[] data = tmpBaos.toByteArray();
        baos.write(data);
    }

    @Override
    public String toString() {
        return String.format("ErrorMessage [ErrorCode = %s; RoutingContext = %s;"
                + "AffectedPointCode = %s; NetworkAppearance = %s; DiagnosticInformation = %s]",
                errorCode, routingContext, affectedPointCode, networkAppearance,
                diagnosticInformation);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        while (bais.available() > 0) {
            ParameterTag tag = ParameterTag.getInstance(bais.readParameterTag());

            switch (tag) {
                case ERROR_CODE:
                    errorCode = new ErrorCode();
                    errorCode.decode(bais);
                    break;
                case ROUTING_CONTEXT:
                    routingContext = new RoutingContext();
                    routingContext.decode(bais);
                    break;
                case NETWORK_APPEARANCE:
                    networkAppearance = new NetworkAppearance();
                    networkAppearance.decode(bais);
                    break;
                case AFFECTED_POINT_CODE:
                    affectedPointCode = new AffectedPointCode();
                    affectedPointCode.decode(bais);
                    break;
                case DIAGNOSTIC_INFORMATION:
                    diagnosticInformation = new DiagnosticInformation();
                    diagnosticInformation.decode(bais);
                    break;
            }
        }
    }

    @Override
    public MessageClass getMessageClass() {
        return this.messageClass;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }

}
