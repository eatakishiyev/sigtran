/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

/**
 * This element contains one of the problem codes which apply to the component
 * sublayer in general, and which do not relate to any specific component type.
 * All of these are generated by the component sublayer.
 *
 * @author root
 */
public enum GeneralProblem {
    /**
     * The component type is not recognized as being one of those defined in
     * 3.1.
     */
    UNRECOGNIZED_COMPONENT(0),
    /**
     * The elemental structure of a component does not conform to the structure
     * of that component as defined in 3.1/Q.773.
     */
    MISTYPED_COMPONENT(1),
    /**
     * The contents of the component do not conform to the encoding rules
     * defined in 4.1/Q.773.
     */
    BADLY_STRUCTURED_COMPONENT(2);
    private int value;

    private GeneralProblem(int value) {
        this.value = value;
    }

    public static GeneralProblem getInstance(int i) {
        switch (i) {
            case 0:
                return UNRECOGNIZED_COMPONENT;
            case 1:
                return MISTYPED_COMPONENT;
            case 2:
                return BADLY_STRUCTURED_COMPONENT;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
