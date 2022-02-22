/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

/**
 *
 * @author eatakishiyev
 */
public class IncorrectAcVersion extends Throwable {

    public IncorrectAcVersion(String msg) {
        super(msg);
    }

    public IncorrectAcVersion(String msg, Throwable cause) {
        super(msg, cause);
    }
}
