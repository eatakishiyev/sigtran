/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

/**
 *
 * @author eatakishiyev
 */
public interface IRequestReportSMSEventArg extends CAPMessage{

    public byte[] getExtensions();

    public void setExtensions(byte[] extensions);
}
