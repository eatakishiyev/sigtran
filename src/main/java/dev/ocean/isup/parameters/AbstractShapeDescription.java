/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.TypeOfShape;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public abstract class AbstractShapeDescription {

    public abstract TypeOfShape getType();

    public abstract void encode(ByteArrayOutputStream baos);

    public abstract void decode(ByteArrayInputStream bais);

}
