/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 *
 * @author eatakishiyev
 */
public class SignallingLinksetSelectorGenerator {

    private final Logger logger = LoggerFactory.getLogger(SignallingLinksetSelectorGenerator.class);
    private final Random random = new Random();
    private final int maxSLS;
    private final LinksetSelectionMode linksetSelectionMode;

    public SignallingLinksetSelectorGenerator(int maxSLS, LinksetSelectionMode linksetSelectionMode) {
        this.maxSLS = maxSLS;
        this.linksetSelectionMode = linksetSelectionMode;
    }

    public int generate() {
        switch (linksetSelectionMode) {
            case EVEN:
                int sls = random.nextInt(maxSLS / 2) * 2;
                if (logger.isDebugEnabled()) {
                    logger.debug("EVEN linksetSelectionMode : " + sls);
                }
                return sls;
            case ODD:
                sls = random.nextInt(maxSLS / 2) * 2 + 1;
                if (logger.isDebugEnabled()) {
                    logger.debug("ODD linksetSelectionMode : " + sls);
                }
                return sls;
            default:
                sls = random.nextInt(maxSLS);
                if (logger.isDebugEnabled()) {
                    logger.debug("Random slsSelectionMode : " + sls);
                }
                return sls;
        }
    }

    public enum LinksetSelectionMode {

        EVEN,
        ODD,
        NONE;
    }
}
