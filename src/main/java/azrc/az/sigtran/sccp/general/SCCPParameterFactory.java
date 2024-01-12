/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import java.io.IOException;
import java.io.Serializable;

import azrc.az.sigtran.sccp.parameters.HopCounter;
import azrc.az.sigtran.sccp.parameters.ReturnCause;
import azrc.az.sigtran.sccp.parameters.Segmentation;

/**
 *
 * @author root
 */
public class SCCPParameterFactory implements Serializable {

    private SCCPParameterFactory(){
        
    }
    public static Segmentation createSegmentation() {
        return new Segmentation();
    }

    public static HopCounter createHopCounter(int hopCount) throws IOException {
        return new HopCounter(hopCount);
    }

    public static ReturnCause createReturnCause(int cause) {
        return new ReturnCause(cause);
    }
}
