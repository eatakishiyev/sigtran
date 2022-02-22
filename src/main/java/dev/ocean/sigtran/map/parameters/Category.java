/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * Category ::= OCTET STRING (SIZE (1))
 * -- The internal structure is defined in ITU-T Rec Q.763.
 *
 * @author eatakishiyev
 */
public class Category implements MAPParameter {
    
    private Categories category;
    
    public Category() {
    }
    
    public Category(Categories category) {
        this.category = category;
    }
    
    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(tagClass, tag, category.value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
    
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }
    
    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            if (length != 1) {
                throw new IncorrectSyntaxException("Unexpected length. Expecting length is 1 , found " + length);
            }
            this.category = Categories.getInstance(ais.read());
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
    
    public void decode(ByteArrayInputStream bais) throws IOException {
        int val = bais.read();
        this.category = Categories.getInstance(val);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category = ").append(category);
        return sb.toString();
    }

    /**
     * @return the category
     */
    public Categories getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Categories category) {
        this.category = category;
    }
    
    public enum Categories {
        
        CALLING_PARTY_CATEGORY_UNKNOWN(0),
        OPERATOR_LANG_FRENCH(1),
        OPERATOR_LANG_ENGLISH(2),
        OPERATOR_LANG_GERMAN(3),
        OPERATOR_LANG_RUSSIAN(4),
        OPERATOR_LANG_SPANISH(5),
        NATIONAL_OPERATOR(9),
        ORDINARY_CALLING_SUBSCRIBER(10),
        CALLING_SUBSCRIBER_WITH_PRIORITY(11),
        DATA_CALL(12),
        TEST_CALL(13),
        PAYPHONE(15),
        RESERVED(16);
        private int value;
        
        private Categories(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        public static Categories getInstance(int value) {
            switch (value) {
                case 0:
                    return CALLING_PARTY_CATEGORY_UNKNOWN;
                case 1:
                    return OPERATOR_LANG_FRENCH;
                case 2:
                    return OPERATOR_LANG_ENGLISH;
                case 3:
                    return OPERATOR_LANG_GERMAN;
                case 4:
                    return OPERATOR_LANG_RUSSIAN;
                case 5:
                    return OPERATOR_LANG_SPANISH;
                case 9:
                    return NATIONAL_OPERATOR;
                case 10:
                    return ORDINARY_CALLING_SUBSCRIBER;
                case 11:
                    return CALLING_SUBSCRIBER_WITH_PRIORITY;
                case 12:
                    return DATA_CALL;
                case 13:
                    return TEST_CALL;
                case 15:
                    return PAYPHONE;
                default:
                    return RESERVED;
            }
        }
    }
    
}
