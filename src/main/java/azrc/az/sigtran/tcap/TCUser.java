/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

import azrc.az.sigtran.tcap.primitives.tc.TCLCancel;
import azrc.az.sigtran.tcap.primitives.tc.TCContinue;
import azrc.az.sigtran.tcap.primitives.tc.TCPAbort;
import azrc.az.sigtran.tcap.primitives.tc.TCUAbort;
import azrc.az.sigtran.tcap.primitives.tc.TCEnd;
import azrc.az.sigtran.tcap.primitives.tc.TCBegin;
import java.io.Serializable;
import azrc.az.sigtran.tcap.primitives.tr.TRNotice;

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
