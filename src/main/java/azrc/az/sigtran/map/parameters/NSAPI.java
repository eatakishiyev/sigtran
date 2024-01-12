/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public class NSAPI extends INTEGER {

    public NSAPI() {
        super();
    }

    public NSAPI(int value) {
        super(value);
    }

    @Override
    public int getMin() {
        return 0;
    }

    @Override
    public int getMax() {
        return 15;
    }

}
