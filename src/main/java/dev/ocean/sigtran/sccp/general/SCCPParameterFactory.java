/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

import java.io.IOException;
import java.io.Serializable;
import dev.ocean.sigtran.sccp.parameters.HopCounter;
import dev.ocean.sigtran.sccp.parameters.ReturnCause;
import dev.ocean.sigtran.sccp.parameters.Segmentation;

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
