/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class SSSubscriptionOption {
//
//    SS-SubscriptionOption ::= CHOICE { 
//          cliRestrictionOption    [2] CliRestrictionOption, 
//          overrideCategory        [1] OverrideCategory} 

    private CliRestrictionOption cliRestrictionOption = null;
    private OverrideCategory overrideCategory = null;
    public static final int CLI_RESTRICTION_OPTION_TAG = 0x02;
    public static final int OVERRIDE_CATEGORY = 0x01;

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int value = (int) ais.readInteger();
            
            switch (ais.getTag()) {
                case CLI_RESTRICTION_OPTION_TAG:
                    this.cliRestrictionOption = CliRestrictionOption.getInstance(value);
                    break;
                case OVERRIDE_CATEGORY:
                    this.overrideCategory = OverrideCategory.getInstance(value);
                    break;
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IOException, AsnException {
        if (cliRestrictionOption != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, CLI_RESTRICTION_OPTION_TAG, cliRestrictionOption.getValue());
        }
        if (overrideCategory != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, OVERRIDE_CATEGORY, overrideCategory.getValue());
        }
    }

    /**
     * @return the cliRestrictionOption
     */
    public CliRestrictionOption getCliRestrictionOption() {
        return cliRestrictionOption;
    }

    /**
     * @param cliRestrictionOption the cliRestrictionOption to set
     */
    public void setCliRestrictionOption(CliRestrictionOption cliRestrictionOption) {
        this.cliRestrictionOption = cliRestrictionOption;
        this.overrideCategory = null;
    }

    /**
     * @return the overrideCategory
     */
    public OverrideCategory getOverrideCategory() {
        return overrideCategory;
    }

    /**
     * @param overrideCategory the overrideCategory to set
     */
    public void setOverrideCategory(OverrideCategory overrideCategory) {
        this.overrideCategory = overrideCategory;
        this.cliRestrictionOption = null;
    }

    public enum CliRestrictionOption {

        PERMANENT(0),
        TEMPORARY_DEFAULT_RESTRICTED(1),
        TEMPORARY_DEFAULT_ALLOWED(2);
        private final int value;

        private CliRestrictionOption(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        public static CliRestrictionOption getInstance(int value) {
            switch (value) {
                case 0:
                    return PERMANENT;
                case 1:
                    return TEMPORARY_DEFAULT_RESTRICTED;
                case 2:
                    return TEMPORARY_DEFAULT_ALLOWED;
                default:
                    return null;
            }
        }
    }

    public enum OverrideCategory {

        OVERRIDE_ENABLED(0),
        OVERRIDE_DISABLED(1);
        private  final int value;

        private OverrideCategory(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        public static OverrideCategory getInstance(int value) {
            switch (value) {
                case 0:
                    return OVERRIDE_ENABLED;
                case 1:
                    return OVERRIDE_DISABLED;
                default:
                    return null;
            }
        }
    }
}
