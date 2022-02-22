/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap;

import dev.ocean.sigtran.tcap.primitives.tc.TCLCancel;
import dev.ocean.sigtran.tcap.primitives.tc.TCContinue;
import dev.ocean.sigtran.tcap.primitives.tc.TCPAbort;
import dev.ocean.sigtran.tcap.primitives.tc.TCUAbort;
import dev.ocean.sigtran.tcap.primitives.tc.TCEnd;
import dev.ocean.sigtran.tcap.primitives.tc.TCBegin;
import java.io.Serializable;
import dev.ocean.sigtran.tcap.primitives.tr.TRNotice;

/**
 *
 * @author root
 */
public interface TCUser extends Serializable {

    public void onContinue(TCContinue indication);

    public void onPAbort(TCPAbort indication);

    public void onBegin(byte[] encodedData, TCBegin indication);

    public void onEnd(TCEnd indication);

    public void onUAbort(TCUAbort indication);

    public void onNotice(TRNotice notice);

    public void onDialogueTimeOut(Long dialogueId);

    public void onTCLCancel(TCLCancel indication);
    
    public void setTCAPProvider(TCAPProvider provider);
}
