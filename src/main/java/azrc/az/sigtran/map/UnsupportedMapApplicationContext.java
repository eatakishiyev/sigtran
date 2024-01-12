/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

import azrc.az.sigtran.map.parameters.MAPApplicationContextImpl;

/**
 *
 * @author eatakishiyev
 */
public class UnsupportedMapApplicationContext extends Exception {

    private final String message;
    private MAPApplicationContextImpl alternativeAc;

    public UnsupportedMapApplicationContext(String message, MAPApplicationContextImpl alternativeAc) {
        this.message = message;
        this.alternativeAc = alternativeAc;
    }

    public UnsupportedMapApplicationContext(String message) {
        this.message = message;
    }

    /**
     * @return the message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return the alternativeAc
     */
    public MAPApplicationContextImpl getAlternativeAc() {
        return alternativeAc;
    }
}
