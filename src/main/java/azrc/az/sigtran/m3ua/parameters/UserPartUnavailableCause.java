/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import azrc.az.sigtran.m3ua.ServiceIdentificator;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 *
 * @author root
 */
public class UserPartUnavailableCause implements Parameter {

    /**
     *
     */
    private ParameterTag tag = ParameterTag.USER_CAUSE;
    private Cause cause;
    private ServiceIdentificator user;

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        bais.readParameterLength();
        int cs = bais.read();
        cs = cs << 8;
        cs = cs | bais.read();
        this.cause = Cause.getInstance(cs);

        int us = bais.read();
        us = us << 8;
        us = us | bais.read();
        this.user = ServiceIdentificator.getInstance(us);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream(272);
        tmpBaos.write((cause.value() >> 8) & 0xFF);
        tmpBaos.write(cause.value() & 0xFF);

        tmpBaos.write((user.value() >> 8) & 0xFF);
        tmpBaos.write(user.value() & 0xFF);

        baos.encode(tag, tmpBaos.toByteArray());
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the Cause
     */
    public Cause getCause() {
        return cause;
    }

    /**
     * @param cause the Cause to set
     */
    public void setCause(Cause cause) {
        this.cause = cause;
    }

    /**
     * @return the User
     */
    public ServiceIdentificator getUser() {
        return user;
    }

    /**
     * @param user the User to set
     */
    public void setUser(ServiceIdentificator user) {
        this.user = user;
    }
}
