/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.parameters;

import azrc.az.sigtran.sccp.general.Encodable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class Importance implements Encodable {

    public static final int PARAMETER_NAME = 0x12;
    private int importance;

    public Importance() {
    }

    public Importance(int importance) {
        this.importance = importance;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        baos.write(PARAMETER_NAME);
        baos.write(1);//length of parameter

        if (importance > 7 || importance < 0) {
            throw new IOException("Max value of importance value must be in range [0..7]");
        }
        baos.write(importance & 0x07);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {
        int length = bais.read();

        this.importance = bais.read() & 0x07;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

}
