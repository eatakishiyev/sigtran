/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

/**
 *
 * @author eatakishiyev
 */
public interface CAPProvider {

    public CAPDialogue getDialogue(long dialogueId);

}
