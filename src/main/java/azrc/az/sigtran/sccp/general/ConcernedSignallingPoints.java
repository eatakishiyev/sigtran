/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public class ConcernedSignallingPoints implements Serializable {

    protected List<RemoteSignallingPoint> concernedSignallingPoints = new ArrayList<>();

    /**
     * @return the concernedSignallingPoint
     */
    public List<RemoteSignallingPoint> getConcernedSignallingPoint() {
        return concernedSignallingPoints;
    }

    public void addConcernedSignallingPoint(RemoteSignallingPoint concernedSpName) throws IOException {
        RemoteSignallingPoint concernedSp = this.getConcernedSignallingPoint(concernedSpName.getName());
        if (concernedSp != null) {
            throw new IOException("RemoteSp already registered as concerned " + concernedSpName);
        }
        this.concernedSignallingPoints.add(concernedSpName);
    }

    public void removeConcernedSignallingPoint(String remoteSpName) throws IOException {
        RemoteSignallingPoint concernedSp = this.getConcernedSignallingPoint(remoteSpName);

        this.concernedSignallingPoints.remove(concernedSp);
    }

    public RemoteSignallingPoint getConcernedSignallingPoint(String concernedSpName) {
        for (int i = 0; i < this.concernedSignallingPoints.size(); i++) {
            RemoteSignallingPoint concernedSp = this.concernedSignallingPoints.get(i);
            if (concernedSp.getName().equals(concernedSpName)) {
                return concernedSp;
            }
        }
        return null;
    }

}
